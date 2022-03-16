package dev.abelab.rdid.helper.table

import groovy.transform.ToString

@ToString
class Row {

    List values = []

    Row or(arg) {
        values.add(arg)
        this
    }
}