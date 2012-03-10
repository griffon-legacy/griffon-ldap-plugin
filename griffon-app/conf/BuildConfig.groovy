griffon.project.dependency.resolution = {
    inherits("global")
    log "warn" 
    repositories {
        griffonHome()
        mavenCentral()
        mavenRepo 'http://repository.springsource.com/maven/bundles/release'
        String basePath = pluginDirPath? "${pluginDirPath}/" : ''
        flatDir name: "ldapLibDir", dirs: ["${basePath}lib"]
    }
    dependencies {
        compile('org.springframework.ldap:org.springframework.ldap:1.3.0.RELEASE',
                'commons-lang:commons-lang:2.6',
                'org.codehaus:gldapo:0.8.5',
                'com.ldaley:injecto:0.7',
                'com.sun:ldapbp:1.0') {
            transitive = false
        }
    }
}

griffon {
    doc {
        logo = '<a href="http://griffon.codehaus.org" target="_blank"><img alt="The Griffon Framework" src="../img/griffon.png" border="0"/></a>'
        sponsorLogo = "<br/>"
        footer = "<br/><br/>Made with Griffon (@griffon.version@)"
    }
}

log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    appenders {
        console name: 'stdout', layout: pattern(conversionPattern: '%d [%t] %-5p %c - %m%n')
    }

    error 'org.codehaus.griffon',
          'org.springframework',
          'org.apache.karaf',
          'groovyx.net'
    warn  'griffon'
}