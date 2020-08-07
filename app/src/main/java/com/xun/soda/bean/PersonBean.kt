package com.xun.soda.bean

/**
 * @Description:
 * @Author:         xwang
 * @CreateDate:     2020/8/5
 */

class PersonBean {
    var name: String = ""
    var sex: Int = 0
    var age: Int = 0
    var content: String = ""

    constructor(name: String, sex: Int, age: Int, content: String) {
        this.name = name
        this.sex = sex
        this.age = age
        this.content = content
    }
}