package com.android.kotlincrud

import com.android.kotlincrud.models.Note
import org.junit.Test

import org.junit.Assert.*

class NoteAdapterTest {

    var notes: ArrayList<Note> = ArrayList()

    @Test
    fun addNote() {
        notes.add(Note(1,"testing note 1"))
        notes.add(Note(2,"testing note 2"))
    }

    @Test
    fun refreshNotes() {
        addNote()
        assertEquals(1, notes[0].id)
        assertEquals("testing note 2", notes[1].title)
    }

    @Test
    fun deleteNote() {
        refreshNotes()
        assertNotNull(notes[1])
        assertEquals(2, notes.size)
        notes.remove(notes[1])
        assertEquals(1, notes.size)
    }
}