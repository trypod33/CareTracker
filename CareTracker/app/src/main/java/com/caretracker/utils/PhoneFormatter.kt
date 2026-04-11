package com.caretracker.utils

/**
 * Utility for formatting and stripping phone numbers.
 *
 * formatPhone("5551234567")     -> "(555) 123-4567"
 * formatPhone("15551234567")    -> "(555) 123-4567"  (strips leading 1)
 * formatPhone("555-123-4567")   -> "(555) 123-4567"
 * formatPhone("(555) 123-4567") -> "(555) 123-4567"  (idempotent)
 * formatPhone("")               -> ""               (no change)
 * formatPhone("123")            -> "123"             (too short, returned as-is)
 */
object PhoneFormatter {

    /** Strip all non-digit characters and return digits only. */
    fun digitsOnly(raw: String): String = raw.filter { it.isDigit() }

    /**
     * Format a raw phone string as (000) 000-0000.
     * If the number cannot be normalised to 10 digits it is returned unchanged.
     */
    fun formatPhone(raw: String): String {
        if (raw.isBlank()) return raw
        var digits = digitsOnly(raw)
        // Strip leading country code "1" for US numbers
        if (digits.length == 11 && digits.startsWith("1")) digits = digits.drop(1)
        return if (digits.length == 10) {
            "(${digits.substring(0, 3)}) ${digits.substring(3, 6)}-${digits.substring(6)}"
        } else {
            raw  // Return original if we can't normalise
        }
    }
}
