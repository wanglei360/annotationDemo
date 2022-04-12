package com.ksp_kotlin

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

/** * 创建者：leiwu
 * * 时间：2022/3/3 15:17
 * * 类描述：
 * * 修改人：com.kspcompiler.TestKspCreate
 * * 修改时间：
 * * 修改备注：
 */
class KspSymbolProcessorProvider : SymbolProcessorProvider { // 该类用来创建一个SymbolProcessor类
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return KspProcessor(environment)
    }
}