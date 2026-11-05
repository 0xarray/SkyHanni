package at.hannibal2.skyhanni.events.chat

import at.hannibal2.skyhanni.api.event.SkyHanniEvent

class TabCompletionEvent(
    val leftOfCursor: String,
    val fullText: String,
    private val originalCompletions: List<String>,
    /**
     * The start of the part of [leftOfCursor] that Brigadier is replacing.
     *
     * Unlike the last whitespace-delimited word, this also contains the complete
     * value of a greedy argument.
     */
    val suggestionStart: Int,
) : SkyHanniEvent() {
    val currentArgument = leftOfCursor.substring(suggestionStart)
    val lastWord = leftOfCursor.substringAfterLast(' ')
    private val additionalSuggestions = mutableSetOf<String>()

    fun addSuggestion(suggestion: String) {
        val completionPrefix = currentArgument
        if (!suggestion.startsWith(completionPrefix, ignoreCase = true) &&
            !suggestion.startsWith(completionPrefix.removePrefix("/"), ignoreCase = true)
        ) return
        val adjustedSuggestion = suggestion.removePrefix("/")
        additionalSuggestions.add(adjustedSuggestion)
    }

    fun addSuggestions(suggestions: Iterable<String>) {
        suggestions.forEach(this::addSuggestion)
    }

    val command = if (leftOfCursor.startsWith("/"))
        leftOfCursor.substring(1).substringBefore(" ").lowercase()
    else ""

    fun isCommand(commandName: String): Boolean {
        return commandName.equals(command, ignoreCase = true)
    }

    fun intoSuggestionArray(): Array<String>? {
        if (additionalSuggestions.isEmpty()) return null
        return (originalCompletions + additionalSuggestions).toTypedArray()
    }
}
