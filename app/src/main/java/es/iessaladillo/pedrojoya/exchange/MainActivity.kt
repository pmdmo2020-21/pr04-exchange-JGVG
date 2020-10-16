package es.iessaladillo.pedrojoya.exchange

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.iessaladillo.pedrojoya.exchange.Currency.*
import es.iessaladillo.pedrojoya.exchange.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    private lateinit var b: MainActivityBinding
    private lateinit var txtAmountWatcher: TextWatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = MainActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        setupViews()
    }

    override fun onStart(){
        super.onStart()

        txtAmountWatcher = object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s!!.isEmpty()){
                    setupTxtAmount()
                }
            }
        }
        b.txtAmount.addTextChangedListener(txtAmountWatcher)
    }

    private fun setupViews() {
        //Funcionality
        b.rdgFirstCurrency.setOnCheckedChangeListener { _, checkedId -> updateFirstImageAndRadioButtons(checkedId)}
        b.rdgSecondCurrency.setOnCheckedChangeListener { _, checkedId -> updateSecondImageAndRadioButtons(checkedId)}
        b.clickButton.setOnClickListener { this.currency() }
        b.txtAmount.setOnEditorActionListener { _, _, _ -> txtAmountOnEditorAction() }

        //Values
        setupTxtAmount()
        b.rdgFirstCurrency.check(b.rdgFirstCurrency.getChildAt(1).id)
        b.rdgSecondCurrency.check(b.rdgSecondCurrency.getChildAt(0).id)

    }

    // Centralizes the editText setup
    private fun setupTxtAmount(){
        b.txtAmount.requestFocus()
        b.txtAmount.setText("0")
        b.txtAmount.selectAll()
    }

    // Update image by changing radiobuttons and enable/disable radiobuttons (for first radiogroup).
    private fun updateFirstImageAndRadioButtons(checkedId: Int) = when (checkedId) {
        b.rdbF01.id -> {
            b.imgFCurrency.setImageResource(DOLLAR.drawableResId)
            b.rdbS01.setEnabled(false)
            b.rdbS02.setEnabled(true)
            b.rdbS03.setEnabled(true)
        }
        b.rdbF02.id -> {
            b.imgFCurrency.setImageResource(EURO.drawableResId)
            b.rdbS01.setEnabled(true)
            b.rdbS02.setEnabled(false)
            b.rdbS03.setEnabled(true)
        }else -> {
            b.imgFCurrency.setImageResource(POUND.drawableResId)
            b.rdbS01.setEnabled(true)
            b.rdbS02.setEnabled(true)
            b.rdbS03.setEnabled(false)
        }
    }

    // Update image by changing radiobuttons and enable/disable radiobuttons (for second radiogroup).
    private fun updateSecondImageAndRadioButtons(checkedId: Int) = when (checkedId) {
        b.rdbS01.id -> {
            b.imgSCurrency.setImageResource(DOLLAR.drawableResId)
            b.rdbF01.setEnabled(false)
            b.rdbF02.setEnabled(true)
            b.rdbF03.setEnabled(true)
        }
        b.rdbS02.id -> {
            b.imgSCurrency.setImageResource(EURO.drawableResId)
            b.rdbF01.setEnabled(true)
            b.rdbF02.setEnabled(false)
            b.rdbF03.setEnabled(true)
        }else -> {
            b.imgSCurrency.setImageResource(POUND.drawableResId)
            b.rdbF01.setEnabled(true)
            b.rdbF02.setEnabled(true)
            b.rdbF03.setEnabled(false)
        }
    }

    // Calculates currency conversion according to user options.
    private fun currency() {
        val rb_text_value_currency_from = findViewById<RadioButton>(b.rdgFirstCurrency.checkedRadioButtonId).getText().toString()
        val rb_text_value_currency_to = findViewById<RadioButton>(b.rdgSecondCurrency.checkedRadioButtonId).getText().toString()
        val amount = b.txtAmount.text.toString().toDouble()

        lateinit var currency_from: Currency
        lateinit var currency_to: Currency
        var result = 0.0

        when (rb_text_value_currency_from) {
            "Dollar" -> when (rb_text_value_currency_to){
                    "Euro" ->{
                        currency_from = DOLLAR
                        currency_to = EURO
                        result = EURO.fromDollar(amount)
                    }
                    "Pound" ->{
                        currency_from = DOLLAR
                        currency_to = POUND
                        result = POUND.fromDollar(amount)
                    }
            }
            "Euro" -> when (rb_text_value_currency_to){
                    "Dollar" ->{
                        currency_from = EURO
                        currency_to = DOLLAR
                        result = EURO.toDollar(amount)
                    }
                    "Pound" ->{
                        currency_from = EURO
                        currency_to = POUND
                        result = EURO.toDollar(amount)
                        result = POUND.fromDollar(result)
                    }
            }
            "Pound" -> when (rb_text_value_currency_to){
                "Dollar" ->{
                    currency_from = POUND
                    currency_to = DOLLAR
                    result = POUND.toDollar(amount)
                }
                "Euro" ->{
                    currency_from = POUND
                    currency_to = EURO
                    result = POUND.toDollar(amount)
                    result = EURO.fromDollar(result)
                }
            }
        }
        showResult(amount, result, currency_from, currency_to)
    }

    // Show the result of the conversion with the desired format through the Ime Options
    private fun txtAmountOnEditorAction(): Boolean {
        currency()
        return true
    }

    // Shows the result of the conversion with the desired format.
    private fun showResult(amount: Double, result: Double, currency_from: Currency, currency_to: Currency){
        SoftInputUtils.hideSoftKeyboard(b.clickButton)
        Toast.makeText(this, getString(R.string.result_currency,amount,currency_from.symbol,result,currency_to.symbol),Toast.LENGTH_SHORT).show()
    }

}