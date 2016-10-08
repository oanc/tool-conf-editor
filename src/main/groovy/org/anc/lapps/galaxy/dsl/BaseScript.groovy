package org.anc.lapps.galaxy.dsl

/**
 * @author Keith Suderman
 */
abstract class BaseScript extends Script {
    static {
        Collection.metaClass.map = { delegate.collect it }
        Collection.metaClass.filter = { delegate.grep it }
        Collection.metaClass.reduce = { acc, value -> delegate.inject(acc,value)}
    }
}
