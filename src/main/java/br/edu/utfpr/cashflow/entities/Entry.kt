package br.edu.utfpr.cashflow.entities

data class Entry(var _id: Int, var type: String, var source: String, var value: Double, var launchDate: String)
