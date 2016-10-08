package org.anc.lapps.galaxy.dsl

import groovy.util.logging.Slf4j

/**
 * @author Keith Suderman
 */
@Slf4j("logger")
class SectionDelegate {

    Node section

    public SectionDelegate(Node section) {
        this.section = section
    }

    void tool(String path) {
        logger.debug("Declaring tool {}", path)
        new Node(section, 'tool', [file:path])
    }

    void tools(List<String> tools) {
        logger.debug("Declaring a list of tools")
        tools.each {
            new Node(section, 'tool', [file:it])
        }
    }

    void tools(String... tools) {
        logger.debug("Declaring multiple tools")
        tools.each {
            new Node(section, 'tool', [file:it])
        }
    }

    void propertyMissing(String name) {
        //logger.warn("Missing property: {}", name)
        throw new MissingPropertyException(name)
    }

    void propertyMissing(String name, String value) {
        logger.warn("Failure to set property {} = {}", name, value)
    }

    void methodMissing(String name, args) {
//        logger.warn("Missing method {}", name)
        throw new MissingMethodException(name, args)
    }
}
