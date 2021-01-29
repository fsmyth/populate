package com.example.populate.data

import java.util.*
//Sample data for injection into the DB
class SampleDataProvider {

    companion object {
        private val sampleName1 = "Jim"
        private val sampleDesc1 = """
            Jim Mathias Pigeon is a 47-year-old town counsellor who enjoys extreme ironing, baking and running. He is generous and considerate
        """.trimIndent()
        private val sampleNotes1 = """
            He is addicted to shopping, something which his friend Reginald Aiden Flores pointed out when he was 16. The problem intensified in 1992. Tim has lost four jobs as a result of his addiction, specifically: golf caddy, tea maker, admin assistant and secretary at a law firm.
        """.trimIndent()

        private val sampleName2 = "Tim"
        private val sampleDesc2 = """
            Tim Phil Barlow is a 25-year-old local activist whose life is dominated by solving the murder of his friend, Helena Campbell. Helena was strangled in 2012 and the killer was never brought to justice.
        """.trimIndent()
        private val sampleNotes2 = """
            He is currently single. His most recent romance was with a sales assistant called Byron Arnold Sullivan, who was 4 years older than him. They broke up because Byron couldn't deal with Jim's obsession with Helena's death.
        """.trimIndent()

        private val sampleName3 = "Maire"
        private val sampleDesc3 = """
            Maire Jenna Doop is a 19-year-old teenager who enjoys praying, travelling and tennis. She is friendly and energetic, but can also be very stingy and a bit dull.
        """.trimIndent()
        private val sampleNotes3 = """
            She grew up in an upper class neighbourhood. Her parents separated when she was small, but remained friends and provided a happy, stable home.
        """.trimIndent()

        private fun getDate(diff: Long): Date {
            return Date(Date().time + diff)
        }

        //Convert the strings above into character entities and compile them into an arraylist.
        fun getChars() = arrayListOf(
                CharacterEntity(getDate(0), sampleName1, sampleDesc1, sampleNotes1),
                CharacterEntity(getDate(1), sampleName2, sampleDesc2, sampleNotes2),
                CharacterEntity(getDate(2), sampleName3, sampleDesc3, sampleNotes3)
        )
    }
}