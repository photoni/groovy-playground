package util

class DateUtil {
	def static parse = { str -> Date.parse("YYYY-M-D", str) }
	def static parseAsLong = { str -> Date.parse("YYYY-M-D", str).getTime() }
}
