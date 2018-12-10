package com.android.kotlincrud

import com.android.kotlincrud.models.Note
import org.junit.Test

import org.junit.Assert.*

class NoteAdapterTest {

    private var notes: ArrayList<Note> = ArrayList()

    @Test
    fun addNote() {
        notes.add(Note(1,"note 1"))
        notes.add(Note(2,"note 2"))
    }

    @Test
    fun readNotes() {
        addNote()
        assertEquals(1, notes[0].id)
        assertEquals("note 2", notes[1].title)
    }

    @Test
    fun updateNote() {
        addNote()
        assertEquals("note 2", notes[1].title)
        notes[1].title = "updated note 2"
        assertEquals("updated note 2", notes[1].title)
    }

    @Test
    fun deleteNote() {
        addNote()
        assertNotNull(notes[1])
        assertEquals(2, notes.size)
        notes.remove(notes[1])
        assertEquals(1, notes.size)
    }
}