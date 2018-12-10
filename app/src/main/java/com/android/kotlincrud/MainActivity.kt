package com.android.kotlincrud

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.android.kotlincrud.models.Note
import com.android.kotlincrud.utils.LocaleHelper
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: NoteAdapter
    private lateinit var context: Context

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = NoteAdapter(this.baseContext)
        fab.setOnClickListener { showNewDialog() }

        rv_item_list.layoutManager = LinearLayoutManager(this)
        rv_item_list.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.lang_en -> {
            Toasty.success(this.baseContext, "Changed to English Language", Toast.LENGTH_LONG).show()
            context = LocaleHelper.setLocale(applicationContext, "en")
            recreate()
            true
        }
        R.id.lang_gr -> {
            context = LocaleHelper.setLocale(applicationContext, "el")
            Toasty.success(this.baseContext, "Changed to Greek Language", Toast.LENGTH_LONG).show()
            recreate()
            true
        }
        R.id.refresh_btn -> {
            adapter.refreshNotes()
            Toasty.success(this.baseContext, baseContext.getString(R.string.refreshed), Toast.LENGTH_LONG).show()
            true
        }
        R.id.search_btn -> {
            showSpecificNoteDialog()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun showSpecificNoteDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val input = EditText(this@MainActivity)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.inputType = InputType.TYPE_CLASS_NUMBER
        input.layoutParams = lp

        dialogBuilder.setView(input)
        dialogBuilder.setTitle(R.string.search)
        dialogBuilder.setMessage(R.string.search_by_id)
        dialogBuilder.setPositiveButton(R.string.search) { _, _ ->
            if (input.text.toString() != "") {
                adapter.getSpecificNote(Integer.parseInt(input.text.toString()))
            }
        }
        dialogBuilder.setNegativeButton(R.string.cancel) { dialog, _ ->
            dialog.cancel()
        }
        val b = dialogBuilder.create()
        b.show()
    }

    private fun showNewDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val input = EditText(this@MainActivity)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp

        dialogBuilder.setView(input)
        dialogBuilder.setTitle(resources.getString(R.string.add_note))
        dialogBuilder.setMessage(R.string.enter_name)
        dialogBuilder.setPositiveButton(R.string.save) { _, _ ->
            adapter.addNote(Note(0,input.text.toString()))
        }
        dialogBuilder.setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
            dialog.cancel()
        }
        val b = dialogBuilder.create()
        b.show()
    }
}
