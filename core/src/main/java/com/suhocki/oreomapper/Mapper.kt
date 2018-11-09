package com.suhocki.oreomapper

class Mapper(val converters: Set<Converter<*, *>>) {

    @Suppress("UNCHECKED_CAST")
    inline fun <reified To> map(
        inputValue: Any,
        genericType: Class<out Any>? = null
    ): To = if (inputValue is To) {
        inputValue
    } else {
        val suitableConverter: (Converter<*, *>) -> Boolean = {
            val isSuitableConverter =
                it.fromClass == inputValue.javaClass && To::class.java.isAssignableFrom(it.toClass)

            if (genericType != null)
                it is GenericConverter && isSuitableConverter && it.genericType == genericType
            else isSuitableConverter
        }

        (converters.find(suitableConverter) as Converter<Any, To>).convert(inputValue)
    }
}
