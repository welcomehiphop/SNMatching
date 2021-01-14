package com.samsung.thai.qrsnmatching

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
var qrCode =""
var barCode = ""
var isQrCode = false
var isBarCode = false
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        val barCodeScan = findViewById<Button>(R.id.barCodeScan)
        val qrCodeScan = findViewById<Button>(R.id.qrCodeScan)
        val clear = findViewById<Button>(R.id.clear)

        val resultBarCodeScan = findViewById<TextView>(R.id.resultBarCodeScan)
        val resultQrCodeScan = findViewById<TextView>(R.id.resultQrCodeScan)
        val result = findViewById<TextView>(R.id.result)

        barCodeScan.setOnClickListener {
            isQrCode = false
            isBarCode = true
            IntentIntegrator(this).apply {
                captureActivity = CustomActivity::class.java
                setDesiredBarcodeFormats(IntentIntegrator.CODE_39)
                setCameraId(0)
                setBeepEnabled(false)
                setOrientationLocked(false)
                initiateScan()
            }
        }
        qrCodeScan.setOnClickListener {
            isQrCode = true
            isBarCode = false
            IntentIntegrator(this).apply {
                captureActivity = CustomActivity::class.java
                setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                setCameraId(0)
                setBeepEnabled(false)
                setOrientationLocked(false)
                initiateScan()
            }

        }

        clear.setOnClickListener {
            resultBarCodeScan.text = ""
            resultQrCodeScan.text = ""
            qrCode = ""
            barCode = ""
            result.setBackgroundColor(Color.parseColor("#D3D3D3"))
            result.text = ""
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val results = findViewById<TextView>(R.id.result)
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else if (isBarCode) {
                    val resultBarCodeScan = findViewById<TextView>(R.id.resultBarCodeScan)
                    barCode = result.contents
                    resultBarCodeScan.text = barCode

                    if (barCode.isNotEmpty()) {
                        if(qrCode.isNotEmpty()){
                            if(qrCode.contains("?r=",ignoreCase = true)){
                                var temp = qrCode.split("?r=")
                                var compareString = temp[1].split("&mc=")
                                if (barCode == compareString[0]) {
                                    results.setBackgroundColor(Color.BLUE)
                                    results.text = "OK"
                                    results.visibility = View.VISIBLE
                                } else if (barCode.isNotEmpty() && qrCode.isNotEmpty()) {
                                    results.setBackgroundColor(Color.RED)
                                    results.text = "NG"
                                    results.visibility = View.VISIBLE
                                }
                            }else{
                                resultBarCodeScan.text = "The QR Code is not in the correct format"
                            }
                        }
                    }else{
                        resultBarCodeScan.text = "Barcode is null"
                    }
                } else if (isQrCode) {
                    val resultQrCodeScan = findViewById<TextView>(R.id.resultQrCodeScan)
                    qrCode = result.contents
                    resultQrCodeScan.text = qrCode
                    if (qrCode.isNotEmpty()) {
                        if(barCode.isNotEmpty()){
                            if(qrCode.contains("?r=",ignoreCase = true)){
                                var temp = qrCode.split("?r=")
                                var compareString = temp[1].split("&mc=")
                                if (barCode == compareString[0]) {
                                    results.setBackgroundColor(Color.BLUE)
                                    results.text = "OK"
                                    results.visibility = View.VISIBLE
                                } else if (barCode.isNotEmpty() && qrCode.isNotEmpty()) {
                                    results.setBackgroundColor(Color.RED)
                                    results.text = "NG"
                                    results.visibility = View.VISIBLE
                                }
                            }else{
                                resultQrCodeScan.text = "The QR Code is not in the correct format"
                            }
                        }
                    }else{
                        resultQrCodeScan.text = "QR Code is null"
                    }
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }
}