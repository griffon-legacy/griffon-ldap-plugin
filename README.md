
LDAP client
-----------

Plugin page: [http://artifacts.griffon-framework.org/plugin/ldap](http://artifacts.griffon-framework.org/plugin/ldap)


The Griffon LDAP Plugin uses the [Gldapo][1] library to provide an easy to use,
object oriented, interface to LDAP enabled directories. A direct port of the
[LDAP plugin for Grails][2]. Original plugin made by Luke Daley.

Usage
-----

The [Gldapo site][3] contains the current documentation for Gldapo. Please refer
to that, but beware that initialization/config is a little different in the
Griffon context.

Configuration
-------------

To configure gldapo for use with your Griffon app, you follow the same regular
[config script initialization][4] process, except that you put the config under
the key ldap in your Griffon application config. The most common way to do this
is to use the `griffon-app/conf/Config.groovy` file.

        ldap {
            directories {
                directory1 {
                    url = "ldap://example.com"
                    base = "ou=example,dc=com"
                    userDn = "uid=someuser,ou=example,dc=com"
                    password = "password"
                    searchControls {
                        countLimit = 40
                        timeLimit = 600
                        searchScope = "subtree"
                    }
                }
                directory2 {
                    urls = [ "ldap://s1.example2.com", "ldap://s2.example2.com"]
                    base = "ou=example2,dc=com"
                }
             }
        }

### Schema Classes

This plugin adds the `griffon-app/ldap` directory to your project. You should
put your schema classes in this directory to have them found by the plugin and
automatically registered into your config.

### The ‘gldapo’ bean

This plugin also adds a bean called `gldapo` to your application context. This
bean is the instance of `gldapo.Gldapo` created from the config.

Scripts
-------

 * **create-ldap-schema** - creates a new LDAP schema class in `griffon-app/ldap`

[1]: http://gldapo.codehaus.org/
[2]: http://grails.org/plugin/ldap
[3]: http://gldapo.codehaus.org/
[4]: http://gldapo.codehaus.org/initialization/configscript.html

