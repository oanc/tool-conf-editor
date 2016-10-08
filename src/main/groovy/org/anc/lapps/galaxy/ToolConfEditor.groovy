package org.anc.lapps.galaxy

import groovy.util.logging.Slf4j
import groovy.xml.XmlUtil
import org.anc.lapps.galaxy.dsl.Dsl

/**
 * @author Keith Suderman
 */
@Slf4j("logger")
class ToolConfEditor {

    static final String COPY = "Copyright ${0xa9 as char} 2016 The Language Application Grid"

    boolean minimize = false

    void process(File conf, File include, String output, List<String> files) {
        logger.info("parsing the config file {}", conf.path)
        XmlParser parser = new XmlParser()
        Node toolbox = parser.parse(conf)

        logger.info("Parsing the DSL files")
        Dsl dsl = new Dsl(toolbox)
        files.each {
            File file = resolve(include, it)
            if (!file) {
                throw new FileNotFoundException(it)
            }
            dsl.parse(file)
        }

        logger.info("serializing the XML")
        //serialize(toolbox)
        if (minimize) {
            println "--minimize has been set."
            toolbox = minimize(toolbox)
        }
        PrintStream out = System.out
        boolean close = false
        if (output) {
            out = new PrintStream(output)
            close = true
        }
        serialize(toolbox, out)
        if (close) {
            out.flush()
            out.close()
        }
    }

    Node minimize(Node node) {
        Node result = new Node(null, node.name())
        node.children().each { Node section ->
            if (section.children().size() > 0) {
                result.append(section)
            }
            else {
                println "Excluding section ${section.attribute('id')}"
            }
        }
        return result
    }

    File resolve(File include, String name) {
        File file = new File(name)
        logger.trace("Resolving {}", file.path)
        if (file.exists()) {
            return file
        }
        if (!file.name.endsWith('.groovy')) {
            file = new File(name + '.groovy')
            logger.trace("Resolving {}", file.path)
            if (file.exists()) {
                return file
            }
        }
        if (include) {
            file = new File(include, name)
            logger.trace("Resolving {}", file.path)
            if (file.exists()) {
                return file
            }
            file = new File(include, name + '.groovy')
            logger.trace("Resolving {}", file.path)
            if (file.exists()) {
                return file
            }
        }
        logger.warn("Include for {} not found", name)
        return null
    }

    void serialize(Node node) {
        serialize(node, System.out)
    }

    void serialize(Node node, OutputStream out) {
        serialize(node, new PrintWriter(out))
    }

    void serialize(Node node, PrintWriter writer) {
        writer.println('<?xml version="1.0" encoding="UTF-8"?>')
        XmlNodePrinter printer = new XmlNodePrinter(writer)
        printer.print(node)
    }

    static void main(String[] args) {

        char copyright = (int) 0xa9

        CliBuilder cli = new CliBuilder()
        cli.usage = '''
java -jar ToolConfEditor.jar [-h|-v] -c <tool_conf.xml> [-o <output_file>]
'''
        cli.header = '''
System for automating changes to Galaxy's tool_conf.xml file.

OPTIONS

'''
        cli.footer = """
If no output (-o) location is specified the result will be echoed to stdout.

$COPY

"""

        cli.h(longOpt:'help', 'Displays this help message')
        cli.v(longOpt:'version', 'Displays the current application version')
        cli.m(longOpt: 'minimize', 'Removes unused sections from the output file')
        cli.c(longOpt:'conf', args:1, 'The path to the tool_conf.xml file to edit')
        cli.o(longOpt:'output', args:1, 'Where the edited file will be saved.')
        cli.i(longOpt:'include', args:1, 'Include directory searched when looking for scripts')

        def params = cli.parse(args)
        if (!params) {
            println "ERROR: Unable to parse the command line parameters."
            cli.usage()
            return
        }

        if (params.h) {
            cli.usage()
            return
        }

        if (params.v) {
            println ""
            println "ToolConfEditor v${Version.version}"
            println COPY
//            println "Copyright $copyright 2016 The Language Application Grid"
            println ""
            return
        }

        if (!params.c) {
            println "ERROR: No tool_conf.xml input file specified."
            return
        }

        File toolConfFile = new File(params.c)
        if (!toolConfFile.exists()) {
            println "Tool conf file not found."
            return
        }

        File includeDir = null
        if (params.i) {
            includeDir = new File(params.i)
            if (!includeDir.exists()) {
                println "Include directory not found."
                return
            }
            if (!includeDir.isDirectory()) {
                println "The include location must be a direcctory."
                return
            }
        }

//        PrintStream out = System.out
//        boolean close = false
//        if (params.o) {
//            File outputFile = new File(params.o)
//            File parent = outputFile.parentFile
//            if (!parent.exists()) {
//                if (!parent.mkdirs()) {
//                    println "Unable to write to ${parent.path}"
//                    return
//                }
//            }
//            out = new PrintStream(outputFile)
//            close = true
//        }

        ToolConfEditor app = new ToolConfEditor()
        app.minimize = params.m
        app.process(toolConfFile, includeDir, params.o, params.arguments())
    }
}
