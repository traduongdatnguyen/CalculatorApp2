package com.example.calculatorapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.calculatorapp.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding :ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        setContentView(binding.root)
    }

    fun buttoninter(view: View){
        startActivity(Intent(this@MainActivity,LearLayoutNew::class.java))
    }
    fun numberAction(view: View){
        if(view is Button) binding.viewTV.append(view.text)
        else binding.viewTV.text = ""
    }

    fun clearAllnumber(view: View){
        binding.viewTV.text = ""
        binding.resultTV.text =""
    }

    fun clearnumberlength(view: View){
        var length = binding.viewTV.length()
        if (length > 0 ) binding.viewTV.text = binding.viewTV.text.subSequence(0,length-1)
    }

    fun equasResult(view: View){
        binding.resultTV.text = calculateResults()

    }

    private fun calculateResults(): String {
        val actionString= actionStringtoArray()
        if (actionString.isEmpty()) return ""

        val timesDivision = timesDivisionCalculators(actionString)
        if (timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)

        return result.toString()
    }

    private fun addSubtractCalculate(passedList: ArrayList<Any>): Float {
        var result = passedList[0] as Float

        for(i in passedList.indices){
            if(passedList[i] is Char && i != passedList.lastIndex){
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float

                if(operator == '+') result += nextDigit
                if (operator == '-') result -= nextDigit
            }
        }

        return result
    }

    private fun actionStringtoArray(): ArrayList<Any> {
        var list = arrayListOf<Any>()
        var addtoString = ""

        for (character in binding.viewTV.text){
            if (character.isDigit() || character == '.') addtoString += character
            else{
                list.add(addtoString.toFloat())
                addtoString = ""
                list.add(character)
            }
        }
        if (addtoString != "") list.add(addtoString.toFloat())
        return list
    }

    private fun timesDivisionCalculators(passedList : ArrayList<Any>): ArrayList<Any> {
        var list = passedList

        while (list.contains('*') || list.contains('/') ){
            list = calcTimesDiv(list)
        }

        return list
    }

    private fun calcTimesDiv(passedlist: ArrayList<Any>): ArrayList<Any> {
        var newList = arrayListOf<Any>()
        var restartInder = passedlist.size
        for (i in passedlist.indices){
            if(passedlist[i] is Char && i != passedlist.lastIndex && i < restartInder ){
                var operator = passedlist[i]
                val prevDigit = passedlist[i - 1] as Float
                val nextDigit = passedlist[i + 1] as Float

                when(operator){
                    '*'-> {
                        newList.add(prevDigit * nextDigit )
                        restartInder = i + 1
                    }
                    '/'-> {
                        newList.add(prevDigit / nextDigit )
                        restartInder = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }

                }
            }
            if(i > restartInder)
                newList.add(passedlist[i])
        }
        return newList
    }

}