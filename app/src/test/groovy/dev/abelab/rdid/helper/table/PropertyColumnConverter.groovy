package dev.abelab.rdid.helper.table

class PropertyColumnConverter {
    Column getProperty(final String property) {
        new Column(name: property)
    }
}
