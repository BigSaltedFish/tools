package io.ztc.tools

object NullUtils {

    /**
     * 列表是否不为空
     */
    fun isNotEmpty(list: List<*>?): Boolean {
        return list != null && list.isNotEmpty()
    }

    /**
     * 字符串是否不为空
     */
    fun isNotEmpty(string: String?):Boolean{
        return string!=null && string!="null" && string.isNotEmpty() && string!=""
    }

    /**
     * 字符串是否为空
     */
    fun isEmpty(string: String?):Boolean{
        return string==null || string.isEmpty() ||string ==""
    }


}