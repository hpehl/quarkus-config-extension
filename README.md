# Shamrock Config Extension

Adds a servlet to a Protean application which lists the configured values for [MicroProfile Configuration](https://microprofile.io/project/eclipse/microprofile-config). The servlet is registered using the path `/config` by default and lists all properties of all config sources. The config sources are sorted descending by ordinal, the properties by name. If no config is defined an empty JSON object is returned. 

A typical output might look like:
```json
{
  "sources": [
    {
      "source": "SysPropConfigSource",
      "ordinal": 400,
      "properties": {
        "file.encoding": "UTF-8",
        "file.separator": "/"
      }
    },
    {
      "source": "EnvConfigSource",
      "ordinal": 300,
      "properties": {
        "EDITOR": "vim",
        "LC_ALL": "en_US.UTF-8"
      }
    },
    {
      "source": "PropertiesConfigSource[source=resource:META-INF/microprofile-config.properties]",
      "ordinal": 100,
      "properties": {
        "greeting.message": "hello",
        "greeting.name": "shamrock"
      }
    }
  ]
}
```
