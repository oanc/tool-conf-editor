# Galaxy tool_conf.xml Editor

The ToolConfigEditor (tce) is a Groovy DSL used to modify Galaxy's `tool_conf.xml` files.  When building custom LAPP/Galaxy appliances we need to update the `tool_conf.xml` file to include just the tools that are present in that appliance.  While installing tools from Galaxy's tool shed will update the `tool_conf.xml` file there are several drawbacks to relying on the tool shed:

1. Not all tools we want to install are available on the tool shed
1. We may want to add a tool to more than once section of the tool_conf.xml

We could use XSL stylesheets to modify the `tool_conf.xml` file, but XSL is rather syntax heavy for our users.  The ToolConfEditor DSL provides a much simpler syntax for expressing how the `tool_conf.xml` file should be modified.

**NOTE** 
Currenly only additive changes may be made. That is, it is not possible to delete tools from a `tool_conf.xml` file.

## Usage

```bash
java -jar ToolConfEditor-x.y.z.jar [-m] -c <template> -o <output file> [-i <include dir>] module [module ...]
```

The examples assume that a script named `tce` is available on the path that call the jar file. See the `install.sh` script for an example of how to generate such a script.  By default the `install.sh` script generates a startup script in `$HOME/bin/tce`.

### Options

**-m**<br/>**--minimize**<br/>
Removes empty section elements from the output.

**-c &lt;template&gt;**<br/>
**--conf &lt;template&gt;**<br/>
The `tool_conf.xml` file to me modified

**-o &lt;output file&gt;**<br/>
**--output &lt;output file&gt;**[optional]<br/>
Where the output will be written.  If this is omitted the output will be written to stdout (System.out)

**-i &lt;include dir&gt;**<br/>
**--include &lt;include dir&gt;** [optional]<br/>
A directory to be searched for files included with the `include` directive.

**module [module...]**<br/>
A list of DSL files that will be processed in order. See the DSL Syntax section below for the syntax of the module files.


## DSL Syntax

The ToolConfEditor DSL is a Groovy DSL so any valid Groovy syntax, and by extension most valid Java syntax, is supported.  However, the ToolConfEditor adds support for the statements

```
id { tool_list }
id 'name', { tool_list }
```

where *id* is the value of the `id` attribute of the &lt;section&gt; element, *name* (if present) is the value of the `name` attribute, and *tool_list* is list of paths to the tool xml files.  The `tool_list` can contain multiple `tool` definitions or defines multiple tools at once with the `tools` statement.  For example:

``` 
foo {
  tool "foo/tool1.xml"
  tool "foo.tool2.xml"
}
bar 'This is a bar', {
  tools "bar/tool1.xml", "bar/tool2.xml"
}
```
This will result in the following `tool_conf.xml`:
```xml
<toolbox>
  <section id="foo" name="Unknown">
    <tool file="foo/tool1.xml"/>
    <tool file="foo/tool2.xml"/>
  </section>
  <section id="bar" name="This is a bar">
    <tool file="bar/tool1.xml"/>
    <tool file="bar/tool2.xml"/>
  </section>
</toolbox>
```
  
