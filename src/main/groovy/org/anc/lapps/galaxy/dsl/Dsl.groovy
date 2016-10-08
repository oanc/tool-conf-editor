package org.anc.lapps.galaxy.dsl

import groovy.util.logging.Slf4j
import org.codehaus.groovy.control.CompilerConfiguration

/**
 * @author Keith Suderman
 */
@Slf4j("logger")
class Dsl {

    GroovyShell shell
    Binding binding
//    File root
    Node toolbox

    public Dsl(Node toolbox) {
        this.toolbox = toolbox
        this.binding = new Binding()

        CompilerConfiguration configuration = new CompilerConfiguration()
        configuration.scriptBaseClass = BaseScript.class.name
        shell = new GroovyShell(binding, configuration)
    }

    void parse(File file) {
        logger.info("DSL parsing {}", file.path)
        Script script = shell.parse(file)
        script.metaClass = getMetaClass(script.class)
        script.run()
    }

    void write() {
        write(System.out)
    }

    void write(OutputStream out) {
        write(new PrintWriter(out))
    }

    void write(PrintWriter writer) {
        new XmlNodePrinter()
    }

    MetaClass getMetaClass(Class theClass) {
        ExpandoMetaClass meta = new ExpandoMetaClass(theClass, false)
        meta.methodMissing = { String id, args ->
            logger.info("Missing method: {}({})", id, args.size())
            Closure closure = null
            String name = 'Unknown'
            if (args.size() == 2) {
                name = args[0]
                logger.debug("Section name {}", name)
                closure = (Closure) args[1]
            }
            else {
                closure = (Closure) args[0]
            }
            Node section = toolbox.children().find { it.attribute('id') == id }
            if (section == null) {
                section = new Node(toolbox, 'section', [id:id, name:name])
            }
            closure.delegate = new SectionDelegate(section)
            closure.resolveStrategy = Closure.DELEGATE_FIRST
            closure()
        }

        meta.initialize()
        return meta
    }
}

