package com.android.kotlincrud

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.android.kotlincrud.api.NoteApiClient
import es.dmoral.toasty.Toasty
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.list_item.view.*

class NoteAdapter (val context: Context) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val client by lazy { NoteApiClient.create() }
    private var notes: ArrayList<Note> = ArrayList()
    init { refreshNotes() }

    class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): NoteAdapter.NoteViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val id_text : String = notes[position].id.toString() + ")"
        holder.view.note_id.text = id_text
        holder.view.name.text = notes[position].title
        holder.view.btnDelete.setOnClickListener { showDeleteDialog(holder, notes[position]) }
        holder.view.btnEdit.setOnClickListener { showUpdateDialog(holder, notes[position]) }
    }

    override fun getItemCount() = notes.size

    fun refreshNotes() {
        client.getNotes()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                notes.clear()
                notes.addAll(result)
                notifyDataSetChanged()
            },{ error ->
                Toasty.error(context, "Refresh error: ${error.message}", Toast.LENGTH_LONG).show()
                Log.e("ERRORS", error.message)
            })
    }

    fun getSpecificNote(note_id: Int) {
        client.getSpecificNote(note_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({  result ->
                notes.clear()
                notes.add(result)
                notifyDataSetChanged() }, { throwable ->
                Toasty.warning(context, "Note with this id not found", Toast.LENGTH_LONG).show()
            })
    }

    private fun updateNote(note: Note) {
        client.updateNote(note.id, note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ refreshNotes() }, { throwable ->
                Toasty.error(context, "Update error: ${throwable.message}", Toast.LENGTH_LONG).show()
            })
    }

    fun addNote(note: Note) {
        client.addNote(note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ refreshNotes() }, { throwable ->
                Toasty.error(context, "Add error: ${throwable.message}", Toast.LENGTH_LONG).show()
            })
    }

    private fun deleteNote(note: Note) {
        client.deleteNote(note.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ refreshNotes() }, { throwable ->
                Toasty.error(context, "Delete error: ${throwable.message}", Toast.LENGTH_LONG).show()
            })
    }

    private fun showUpdateDialog(holder: NoteViewHolder, note: Note) {
        val dialogBuilder = AlertDialog.Builder(holder.view.context)

        val input = EditText(holder.view.context)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
        input.layoutParams = lp
        input.setText(note.title)

        dialogBuilder.setView(input)

        dialogBuilder.setTitle(R.string.update_note)
        dialogBuilder.setPositiveButton(R.string.update) { dialog, whichButton ->
            updateNote(Note(note.id,input.text.toString()))
        }
        dialogBuilder.setNegativeButton(R.string.cancel) { dialog, whichButton ->
            dialog.cancel()
        }
        val b = dialogBuilder.create()
        b.show()
    }

    private fun showDeleteDialog(holder: NoteViewHolder, note: Note) {
        val dialogBuilder = AlertDialog.Builder(holder.view.context)
        dialogBuilder.setTitle(R.string.delete)
        dialogBuilder.setMessage(R.string.delete_conf)
        dialogBuilder.setPositiveButton(R.string.delete) { dialog, whichButton ->
            deleteNote(note)
        }
        dialogBuilder.setNegativeButton(R.string.cancel) { dialog, whichButton ->
            dialog.cancel()
        }
        val b = dialogBuilder.create()
        b.show()
    }
}