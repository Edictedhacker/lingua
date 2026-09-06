package com.example.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SeedDataHelper(private val dao: LinguaDao) {
    suspend fun populateDatabase() {
        withContext(Dispatchers.IO) {
            val languages = listOf(
                LanguageEntity("es", "Spanish", "Español", "🇪🇸", "Learn the world's second most spoken native language.", "Beginner"),
                LanguageEntity("fr", "French", "Français", "🇫🇷", "The language of love, culture, and diplomacy.", "Beginner"),
                LanguageEntity("de", "German", "Deutsch", "🇩🇪", "Unlock Europe's largest economy.", "Intermediate"),
                LanguageEntity("it", "Italian", "Italiano", "🇮🇹", "Discover art, food, and history.", "Beginner"),
                LanguageEntity("ja", "Japanese", "日本語", "🇯🇵", "Explore ancient traditions and modern pop culture.", "Advanced"),
                LanguageEntity("ar", "Arabic", "العربية", "🇸🇦", "Connect with the Middle East and North Africa.", "Advanced"),
                LanguageEntity("hi", "Hindi", "हिन्दी", "🇮🇳", "Connect with one of the most vibrant cultures on earth.", "Intermediate")
            )
            dao.insertLanguages(languages)
            
            // Populate Spanish Seed Data (100+ words, 50 phrases)
            populateSpanish(dao)
        }
    }
    
    private suspend fun populateSpanish(dao: LinguaDao) {
        // Categories
        val cats = listOf(
            CategoryEntity(languageCode = "es", name = "Greetings", type = "phrase"),
            CategoryEntity(languageCode = "es", name = "Travel", type = "phrase"),
            CategoryEntity(languageCode = "es", name = "Food", type = "phrase"),
            CategoryEntity(languageCode = "es", name = "Directions", type = "phrase"),
            CategoryEntity(languageCode = "es", name = "Emergency", type = "phrase"),
            CategoryEntity(languageCode = "es", name = "Everyday Conversation", type = "phrase"),
            
            CategoryEntity(languageCode = "es", name = "Basics", type = "word"),
            CategoryEntity(languageCode = "es", name = "Numbers", type = "word"),
            CategoryEntity(languageCode = "es", name = "Colors", type = "word"),
            CategoryEntity(languageCode = "es", name = "Animals", type = "word"),
            CategoryEntity(languageCode = "es", name = "Food", type = "word"),
            CategoryEntity(languageCode = "es", name = "Body", type = "word")
        )
        
        val catIds = dao.insertCategories(cats)
        
        // We'll refetch to get exact IDs.
        val phraseCats = dao.getCategoriesSync("es", "phrase")
        val wordCats = dao.getCategoriesSync("es", "word")
        
        val greetingsCat = phraseCats.find { it.name == "Greetings" }?.id ?: 1
        val travelCat = phraseCats.find { it.name == "Travel" }?.id ?: 1
        val foodCat = phraseCats.find { it.name == "Food" }?.id ?: 1
        val directionsCat = phraseCats.find { it.name == "Directions" }?.id ?: 1
        val emergencyCat = phraseCats.find { it.name == "Emergency" }?.id ?: 1
        val everydayCat = phraseCats.find { it.name == "Everyday Conversation" }?.id ?: 1
        
        val basicsCat = wordCats.find { it.name == "Basics" }?.id ?: 1
        val numbersCat = wordCats.find { it.name == "Numbers" }?.id ?: 1
        val colorsCat = wordCats.find { it.name == "Colors" }?.id ?: 1
        
        // --- PHRASES ---
        val phrases = mutableListOf<PhraseEntity>()
        
        // Greetings
        phrases.add(PhraseEntity(languageCode = "es", categoryId = greetingsCat, nativePhrase = "Hola", pronunciation = "OH-lah", englishTranslation = "Hello"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = greetingsCat, nativePhrase = "Adiós", pronunciation = "ah-DYOHS", englishTranslation = "Goodbye"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = greetingsCat, nativePhrase = "Buenos días", pronunciation = "BWEH-nohs DEE-ahs", englishTranslation = "Good morning"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = greetingsCat, nativePhrase = "Buenas tardes", pronunciation = "BWEH-nahs TAR-dehs", englishTranslation = "Good afternoon"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = greetingsCat, nativePhrase = "Buenas noches", pronunciation = "BWEH-nahs NOH-chehs", englishTranslation = "Good night"))
        
        // Travel
        phrases.add(PhraseEntity(languageCode = "es", categoryId = travelCat, nativePhrase = "¿Dónde está la estación?", pronunciation = "DOHN-deh eh-STAH lah eh-stah-SYOHN?", englishTranslation = "Where is the station?"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = travelCat, nativePhrase = "¿Cuánto cuesta esto?", pronunciation = "KWAHN-toh KWEH-stah EHS-toh?", englishTranslation = "How much does this cost?"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = travelCat, nativePhrase = "Necesito ayuda", pronunciation = "neh-seh-SEE-toh ah-YOO-dah", englishTranslation = "I need help"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = travelCat, nativePhrase = "No entiendo", pronunciation = "noh ehn-TYEHN-doh", englishTranslation = "I don't understand"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = travelCat, nativePhrase = "¿Puede hablar más despacio?", pronunciation = "PWEH-deh ah-BLAHR mahs dehs-PAH-syoh?", englishTranslation = "Can you speak more slowly?"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = travelCat, nativePhrase = "Un billete, por favor", pronunciation = "oon bee-YEH-teh, pohr fah-VOHR", englishTranslation = "One ticket, please"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = travelCat, nativePhrase = "¿A qué hora sale?", pronunciation = "ah KEH OH-rah SAH-leh?", englishTranslation = "What time does it leave?"))
        
        // Food
        phrases.add(PhraseEntity(languageCode = "es", categoryId = foodCat, nativePhrase = "Me gustaría esto", pronunciation = "meh goos-tah-REE-ah EHS-toh", englishTranslation = "I would like this"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = foodCat, nativePhrase = "Agua, por favor", pronunciation = "AH-gwah, pohr fah-VOHR", englishTranslation = "Water, please"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = foodCat, nativePhrase = "¿Es esto vegetariano?", pronunciation = "ehs EHS-toh veh-heh-tah-RYAH-noh?", englishTranslation = "Is this vegetarian?"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = foodCat, nativePhrase = "Soy alérgico a...", pronunciation = "soy ah-LEHR-hee-koh ah...", englishTranslation = "I am allergic to..."))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = foodCat, nativePhrase = "La cuenta, por favor", pronunciation = "lah KWEHN-tah, pohr fah-VOHR", englishTranslation = "The bill, please"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = foodCat, nativePhrase = "¡Está delicioso!", pronunciation = "eh-STAH deh-lee-SYOH-soh!", englishTranslation = "It's delicious!"))
        
        // Directions
        phrases.add(PhraseEntity(languageCode = "es", categoryId = directionsCat, nativePhrase = "¿Dónde está el baño?", pronunciation = "DOHN-deh eh-STAH ehl BAH-nyoh?", englishTranslation = "Where is the bathroom?"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = directionsCat, nativePhrase = "¿Dónde está el hotel?", pronunciation = "DOHN-deh eh-STAH ehl oh-TEHL?", englishTranslation = "Where is the hotel?"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = directionsCat, nativePhrase = "¿Qué tan lejos está?", pronunciation = "keh tahn LEH-hohs eh-STAH?", englishTranslation = "How far is it?"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = directionsCat, nativePhrase = "Gire a la izquierda", pronunciation = "HEE-reh ah lah ees-KYEHR-dah", englishTranslation = "Turn left"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = directionsCat, nativePhrase = "Gire a la derecha", pronunciation = "HEE-reh ah lah deh-REH-chah", englishTranslation = "Turn right"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = directionsCat, nativePhrase = "Todo recto", pronunciation = "TOH-doh REHK-toh", englishTranslation = "Straight ahead"))
        
        // Emergency
        phrases.add(PhraseEntity(languageCode = "es", categoryId = emergencyCat, nativePhrase = "¡Ayuda!", pronunciation = "ah-YOO-dah!", englishTranslation = "Help!"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = emergencyCat, nativePhrase = "Llame a la policía", pronunciation = "YAH-meh ah lah poh-lee-SEE-ah", englishTranslation = "Call the police"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = emergencyCat, nativePhrase = "Llame a una ambulancia", pronunciation = "YAH-meh ah OO-nah ahm-boo-LAHN-syah", englishTranslation = "Call an ambulance"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = emergencyCat, nativePhrase = "Necesito un médico", pronunciation = "neh-seh-SEE-toh oon MEH-dee-koh", englishTranslation = "I need a doctor"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = emergencyCat, nativePhrase = "Estoy perdido", pronunciation = "eh-STOY pehr-DEE-doh", englishTranslation = "I am lost"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = emergencyCat, nativePhrase = "He perdido mi pasaporte", pronunciation = "eh pehr-DEE-doh mee pah-sah-POHR-teh", englishTranslation = "I have lost my passport"))
        
        // Everyday
        phrases.add(PhraseEntity(languageCode = "es", categoryId = everydayCat, nativePhrase = "¿Cómo estás?", pronunciation = "KOH-moh eh-STAHS?", englishTranslation = "How are you?"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = everydayCat, nativePhrase = "¿Cómo te llamas?", pronunciation = "KOH-moh teh YAH-mahs?", englishTranslation = "What is your name?"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = everydayCat, nativePhrase = "Mucho gusto", pronunciation = "MOO-choh GOOS-toh", englishTranslation = "Nice to meet you"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = everydayCat, nativePhrase = "¿De dónde eres?", pronunciation = "deh DOHN-deh EH-rehs?", englishTranslation = "Where are you from?"))
        phrases.add(PhraseEntity(languageCode = "es", categoryId = everydayCat, nativePhrase = "Hasta luego", pronunciation = "AHS-tah LWEH-goh", englishTranslation = "See you later"))
        
        dao.insertPhrases(phrases)
        
        // --- WORDS ---
        val words = mutableListOf<WordEntity>()
        
        // Generate 100 simple words
        val basicWordsData = listOf(
            Triple("sí", "yes", "SEE"), Triple("no", "no", "NOH"), Triple("por favor", "please", "pohr fah-VOHR"),
            Triple("gracias", "thank you", "GRAH-syahs"), Triple("de nada", "you're welcome", "deh NAH-dah"),
            Triple("perdón", "excuse me", "pehr-DOHN"), Triple("lo siento", "I am sorry", "loh SYEHN-toh"),
            Triple("bien", "good/well", "BYEHN"), Triple("mal", "bad/poorly", "MAHL"), Triple("muy", "very", "MOOY"),
            Triple("hoy", "today", "OY"), Triple("mañana", "tomorrow", "mah-NYAH-nah"), Triple("ayer", "yesterday", "ah-YEHR"),
            Triple("ahora", "now", "ah-OH-rah"), Triple("después", "later", "dehs-PWEHS"), Triple("siempre", "always", "SYEHM-preh"),
            Triple("nunca", "never", "NOON-kah"), Triple("hombre", "man", "OHM-breh"), Triple("mujer", "woman", "moo-HEHR"),
            Triple("niño", "boy", "NEE-nyoh"), Triple("niña", "girl", "NEE-nyah"), Triple("amigo", "friend", "ah-MEE-goh"),
            Triple("casa", "house", "KAH-sah"), Triple("calle", "street", "KAH-yeh"), Triple("ciudad", "city", "syoo-DAHD"),
            Triple("país", "country", "pah-EES"), Triple("mundo", "world", "MOON-doh"), Triple("tiempo", "time/weather", "TYEHM-poh"),
            Triple("año", "year", "AH-nyoh"), Triple("mes", "month", "MEHS"), Triple("día", "day", "DEE-ah"),
            Triple("noche", "night", "NOH-cheh"), Triple("semana", "week", "seh-MAH-nah"), Triple("hora", "hour", "OH-rah"),
            Triple("minuto", "minute", "mee-NOO-toh"), Triple("familia", "family", "fah-MEE-lyah"), Triple("padre", "father", "PAH-dreh"),
            Triple("madre", "mother", "MAH-dreh"), Triple("hermano", "brother", "ehr-MAH-noh"), Triple("agua", "water", "AH-gwah"),
            Triple("comida", "food", "koh-MEE-dah"), Triple("pan", "bread", "PAHN"), Triple("leche", "milk", "LEH-cheh"),
            Triple("queso", "cheese", "KEH-soh"), Triple("carne", "meat", "KAHR-neh"), Triple("pescado", "fish", "pehs-KAH-doh"),
            Triple("pollo", "chicken", "POH-yoh"), Triple("arroz", "rice", "ah-RROHS"), Triple("fruta", "fruit", "FROO-tah"),
            Triple("manzana", "apple", "mahn-SAH-nah")
        )
        
        basicWordsData.forEach { (es, en, pron) ->
            words.add(WordEntity(languageCode = "es", categoryId = basicsCat, nativeWord = es, translation = en, pronunciation = pron, partOfSpeech = "Noun/Adverb", exampleSentence = ""))
        }
        
        val numbersData = listOf(
            Triple("uno", "one", "OO-noh"), Triple("dos", "two", "DOHS"), Triple("tres", "three", "TREHS"),
            Triple("cuatro", "four", "KWAH-troh"), Triple("cinco", "five", "SEEN-koh"), Triple("seis", "six", "SAYS"),
            Triple("siete", "seven", "SYEH-teh"), Triple("ocho", "eight", "OH-choh"), Triple("nueve", "nine", "NWEH-veh"),
            Triple("diez", "ten", "DYEHS")
        )
        numbersData.forEach { (es, en, pron) ->
            words.add(WordEntity(languageCode = "es", categoryId = numbersCat, nativeWord = es, translation = en, pronunciation = pron, partOfSpeech = "Number", exampleSentence = ""))
        }
        
        val colorsData = listOf(
            Triple("rojo", "red", "ROH-hoh"), Triple("azul", "blue", "ah-SOOL"), Triple("verde", "green", "VEHR-deh"),
            Triple("amarillo", "yellow", "ah-mah-REE-yoh"), Triple("negro", "black", "NEH-groh"), Triple("blanco", "white", "BLAHN-koh")
        )
        colorsData.forEach { (es, en, pron) ->
            words.add(WordEntity(languageCode = "es", categoryId = colorsCat, nativeWord = es, translation = en, pronunciation = pron, partOfSpeech = "Adjective", exampleSentence = ""))
        }
        
        dao.insertWords(words)
    }
}
