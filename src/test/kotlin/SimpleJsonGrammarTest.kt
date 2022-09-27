import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SimpleJsonGrammarTest {
    @Test
    fun testParse() {
        val input = "[null]"

        assertEquals("[\n    null\n]", SimpleJsonGrammar.parseToEnd(input).toPlainString())
    }

    @Test
    fun testParse2() {
        val input = "null"

        assertEquals("null", SimpleJsonGrammar.parseToEnd(input).toPlainString())
    }

    @Test
    fun testParse3() {
        val input = "{\"a\": {\"b\": 1}, \"c\": \"test\\\"\"}"
        val expected = """{
            |    "a": {
            |        "b": 1
            |    },
            |    "c": "test\""
            |}
        """.trimMargin()

        assertEquals(expected, SimpleJsonGrammar.parseToEnd(input).toPlainString())
    }

    @Test
    fun testParse4() {
        val input = """{"DokumentGuid":"e88a7d4b-796d-401d-85b7-fda709b2d90e","QuestionnaireResponse":"{\"resourceType\":\"QuestionnaireResponse\",\"id\":\"2af3087b-2482-4cd9-b403-79a67c978b29\",\"meta\":{\"versionId\":\"2\"},\"questionnaire\":\"Questionnaire/605\",\"status\":\"in-progress\",\"item\":[{\"linkId\":\"95408070-4a1d-460a-daf9-5960f66c266e\",\"text\":\"Informasjon Vi ber deg fylle ut og sende inn skjema om helseopplysninger fÃ¸r timeavtalen. Dette er viktig informasjon for de som skal behandle deg. Ta kontakt med din fastlege eller den som har henvist deg til sykehuset dersom du trenger helsehjelp fÃ¸r avtalen din.Â  Din helse kan endre seg over tid, derfor ber vi deg om Ã¥ sende inn skjemaet sÃ¥ snart som mulig, men tidligst tre mÃ¥neder fÃ¸r avtalen. Send inn nytt skjema eller kontakt sykehuset hvis det er endringer fÃ¸r din avtale. Det er vanskelig Ã¥ si hvor lang tid det tar Ã¥ fylle ut skjema. De fleste bruker under 10 minutter, andre lenger, avhengig av sykdommer og medisiner.\"},{\"linkId\":\"4fa349f3-d0b7-40fa-81b9-dd6c5e660ee4\",\"text\":\"## Personopplysninger og pÃ¥rÃ¸rende\",\"item\":[{\"linkId\":\"1cdd79eb-8622-4178-8843-7d4a07139006\",\"text\":\"Navn\",\"answer\":[{\"valueString\":\"Bilal Psa Bull\"}]},{\"linkId\":\"1bb97c2b-e07b-4563-8913-2e13791b545a\",\"text\":\"FÃ¸dselsnummer\",\"answer\":[{\"valueString\":\"12059300359\"}]},{\"linkId\":\"56411a98-c784-4e42-9847-6f7f9c183afa\",\"text\":\"KjÃ¸nn\",\"answer\":[{\"valueString\":\"Mann\"}]},{\"linkId\":\"db2d5465-639d-4329-89dc-021e9a14b8d8\",\"text\":\"Alder\",\"answer\":[{\"valueInteger\":29}]},{\"linkId\":\"1fe1a616-102d-448c-f2f9-340bacf3d393\",\"text\":\"Telefon\",\"answer\":[{\"valueString\":\"+4792491788\"}]},{\"linkId\":\"761b6a41-46ca-4634-9f03-62c0aa7c4498\",\"text\":\"Eventuelt annet telefonnummer vi kan nÃ¥ deg pÃ¥\"},{\"linkId\":\"06b6c0f0-df78-430c-eb94-662f8bf5df12\",\"text\":\"Fastlege\",\"answer\":[{\"valueString\":\"Amalie Aud Sylvia Rolfsen Andreassen\"}]}]},{\"linkId\":\"845cf7b9-69fa-4850-c345-fe7610b5094c\",\"text\":\"Behov for tolk\",\"item\":[{\"linkId\":\"acdb7b1c-e5a9-41b5-b393-822c1906b492\",\"text\":\"Er det nÃ¸dvendig med tolk?\",\"answer\":[{\"item\":[{\"linkId\":\"e12bd9e8-e9cb-4b60-8dc1-981ea2602714\",\"text\":\"Hvilket sprÃ¥k? (Inkludert f.eks tegnsprÃ¥k)\"},{\"linkId\":\"08074bd0-bdf3-4a1e-8239-3799b7895dcb\",\"text\":\"Hvilket land/region/dialekt?\"}]}]},{\"linkId\":\"7c8aa9f4-bfc6-4012-e0ea-5303482234da\",\"text\":\"Er det nÃ¸dvendig med annen type kommunikasjonsstÃ¸tte?\",\"answer\":[{\"item\":[{\"linkId\":\"e60cf3f2-a75d-46ef-bf06-75e72cf785b4\",\"text\":\"Hvilken type?\"},{\"linkId\":\"13b0ecc1-b36e-4dd8-de7d-7dda2c23f66f\"}]}]}]},{\"linkId\":\"d7a84b07-18ee-43a6-8d18-aebe6664ee0d\",\"text\":\"Reise utenfor Norden\",\"item\":[{\"linkId\":\"3c8a6f29-75ec-4fef-881e-72a2fd274862\",\"text\":\"Har du i lÃ¸pet av de siste 12 mÃ¥neder vÃ¦rt i et land utenfor Norden, hvor du har:\",\"answer\":[{\"item\":[{\"linkId\":\"1cb88d50-a494-49c7-c5d1-b14d9b7d6eb8\",\"text\":\"Har du etter hjemkomst tatt en MRSA-prÃ¸ve?\",\"answer\":[{\"item\":[{\"linkId\":\"97e7fad5-354c-44a7-8a09-db72256dc6d3\",\"text\":\"Det er viktig for sykehuset Ã¥ vite om du er smittet med MRSA for Ã¥ forebygge smitte til andre pasienter eller helsepersonell. \\nDu kan ta MRSA-prÃ¸ve hos fastlegen.\"}]}]},{\"linkId\":\"9d21fd32-b414-47e3-c5f7-e7767330d2c8\"}]}]}]},{\"linkId\":\"66c0a9a6-2ad7-4d01-9f5f-0331298be942\",\"text\":\"PÃ¥rÃ¸rende\",\"item\":[{\"linkId\":\"1c115b5d-2963-4072-9462-840933fe8932\",\"text\":\"(Usynlig felt) Til info, Element 5 vises kun for voksne.\"},{\"linkId\":\"c7623f3e-f4fc-4125-d944-59f28527dd3d\",\"text\":\"Fullt navn pÃ¥ nÃ¦rmeste pÃ¥rÃ¸rende\",\"answer\":[{\"item\":[{\"linkId\":\"6da0d408-c207-4f38-862f-f5f53e74d932\",\"text\":\"Relasjon/slektskap\"},{\"linkId\":\"a9365840-2ed4-43e9-be96-deb4f16bd928\",\"text\":\"FÃ¸dselsdato pÃ¥rÃ¸rende\"},{\"linkId\":\"93c20c89-493a-4417-ef49-ac808d267881\"}]}]},{\"linkId\":\"23474c35-fd93-4ba5-f309-058aca527ad1\",\"text\":\"Eventuelt andre pÃ¥rÃ¸rende\"}]},{\"linkId\":\"92adee41-7239-4ee7-cd66-40c70b6c81fc\",\"text\":\"PÃ¥rÃ¸rende\",\"item\":[{\"linkId\":\"f692f88d-96cb-4fc7-835a-0cb957a7507d\",\"text\":\"(Usynlig felt) Til info, Element 6 vises kun for barn for Ã¥ sikre at pÃ¥rÃ¸rende er et obligatorisk felt.\"},{\"linkId\":\"0cb04b9e-b64f-4dd5-8bb6-c3afad9b7bf3\",\"text\":\"Fullt navn pÃ¥ nÃ¦rmeste pÃ¥rÃ¸rende\",\"answer\":[{\"item\":[{\"linkId\":\"2ceb0095-3bdd-4f4c-86e9-07574844d56c\",\"text\":\"Relasjon/slektskap\"},{\"linkId\":\"415119d8-bff6-4dd3-e41b-c1309ae2ef7f\",\"text\":\"FÃ¸dselsdato pÃ¥rÃ¸rende\"},{\"linkId\":\"18b418f1-c952-4db8-8b63-d8d481deff0e\"}]}]},{\"linkId\":\"717293f7-4f45-4ae4-88b7-05e00ae15406\",\"text\":\"Eventuelt andre pÃ¥rÃ¸rende\"}]},{\"linkId\":\"66bf46a4-e6d7-42ca-d0fd-75b0b39bab64\",\"text\":\"Har du barn eller sÃ¸sken under 18 Ã¥r?\",\"answer\":[{\"item\":[{\"linkId\":\"93ac0310-293a-4a7a-8048-153c212b1209\",\"text\":\"(Usynlig felt) Til info, Element 7 vises over 15 Ã¥r.\"},{\"linkId\":\"5c557af2-8956-4c60-ffbf-1b39008e935c\",\"text\":\"Skriv inn navn og fÃ¸delsdato for barn/sÃ¸sken under 18 Ã¥r her:\"},{\"linkId\":\"7ffd0da3-27f2-40ac-8a22-9e232cecae12\"}]}]},{\"linkId\":\"c68cd815-5fbb-4d36-9a02-7eff4be6296d\",\"text\":\"Har du sÃ¸sken under 18 Ã¥r?\",\"answer\":[{\"item\":[{\"linkId\":\"5e6a09b5-9863-4812-93ca-059b0039e292\",\"text\":\"(Usynlig felt) Til info, Element 8 vises under 14 Ã¥r.\"},{\"linkId\":\"bdd41757-56b2-45e6-8301-20dcca72b11d\",\"text\":\"Skriv inn navn og fÃ¸delsdato for barn/sÃ¸sken under 18 Ã¥r her:\"},{\"linkId\":\"eb55c3e8-df83-4084-bc53-2ec76a5b1512\"}]}]},{\"linkId\":\"289025d2-9723-4374-9b72-28222e98dcf1\",\"text\":\"Graviditet og amming\",\"item\":[{\"linkId\":\"4141fdb2-d2d9-4193-85bb-fd031a63998d\",\"text\":\"Er du gravid?\",\"answer\":[{\"item\":[{\"linkId\":\"72ce7be1-8556-420b-8748-fe53f4a8abb5\",\"text\":\"Vennligst oppgi hvor langt du er kommet i graviditeten i antall uker.\"}]}]},{\"linkId\":\"4b55866e-797f-4401-89e0-6f46a25d007b\",\"text\":\"Ammer du?\"}]},{\"linkId\":\"fd0ae03b-4065-4ddb-e16c-c754e9f41da3\",\"text\":\"Sykdommer\",\"item\":[{\"linkId\":\"70f325a0-5d80-4cf9-8364-28d73067abd2\",\"text\":\"Har du, eller har du hatt, noen sykdommer?\",\"answer\":[{\"item\":[{\"linkId\":\"0143e035-e5d2-4529-b52f-405e9ab1835e\",\"text\":\"Har du, eller har du hatt, hjerte-og karsykdom?\",\"answer\":[{\"item\":[{\"linkId\":\"79d8e687-ecc6-484c-8078-258d235178f0\",\"text\":\"Beskrivelse:\"}]}]},{\"linkId\":\"d53ead9d-1c7c-4ded-8f76-eeb704f747b3\",\"text\":\"Har du hÃ¸yt blodtrykk (hypertensjon)?\",\"answer\":[{\"item\":[{\"linkId\":\"8ce77792-310f-4b83-eb82-e455504babd2\",\"text\":\"Beskrivelse:\"}]}]},{\"linkId\":\"72589e99-d486-4d48-c3d5-143f6081f263\",\"text\":\"Har du Ã¸kt blÃ¸dningstendens?\",\"answer\":[{\"item\":[{\"linkId\":\"4bef3fe4-3bf0-4f77-e778-1e15f533eecc\",\"text\":\"Beskrivelse:\"}]}]},{\"linkId\":\"26e093e9-ef3a-4211-9b57-4c8598f170da\",\"text\":\"Har du, eller har du hatt, lungesykdom?\",\"answer\":[{\"item\":[{\"linkId\":\"82dfc132-ab9e-47da-9830-ebb60c18bd12\",\"text\":\"Har du sÃ¸vnapnÃ©?\",\"answer\":[{\"item\":[{\"linkId\":\"333bdf79-6412-43d8-cfdd-69a8d5277c6f\",\"text\":\"Bruker du CPAP- eller BiPAP-maskin?\",\"answer\":[{\"item\":[{\"linkId\":\"6b9ca52a-43c2-415c-89ef-d39ea05ff09c\",\"text\":\"Hvis du skal opereres mÃ¥ du ta med maskinen til sykehuset.\"}]}]}]}]},{\"linkId\":\"4a4843b8-f85c-4395-8be2-f2d04f75a08a\",\"text\":\"Beskrivelse:\"}]}]},{\"linkId\":\"f438d9d3-fe2f-4350-81d3-e3fe4c21d8f0\",\"text\":\"Har du, eller har du hatt, nevrologisk sykdom?\",\"answer\":[{\"item\":[{\"linkId\":\"39e79e07-5a5e-4228-efe5-b83c64da3d1a\",\"text\":\"Beskrivelse:\"}]}]},{\"linkId\":\"f6673908-e230-4f9e-da82-229a7fa3921f\",\"text\":\"Har du, eller har du hatt, nyresykdom?\",\"answer\":[{\"item\":[{\"linkId\":\"e08d8e32-9274-4fb2-9300-0952af121c2b\",\"text\":\"Beskrivelse:\"}]}]},{\"linkId\":\"f5cc0699-2c66-446c-c24f-1eb6d1e6e04b\",\"text\":\"Har du, eller har du hatt leversykdom?\",\"answer\":[{\"item\":[{\"linkId\":\"4e0c1e8f-4bde-43a1-8375-3de03a7edd16\",\"text\":\"Beskrivelse:\"}]}]},{\"linkId\":\"4c1ea354-91a5-4978-e4b2-2c136487b458\",\"text\":\"Har du, eller har du hatt, diabetes?\",\"answer\":[{\"item\":[{\"linkId\":\"061cf9ab-b40e-4357-8515-9cbca1edd0f3\",\"text\":\"Beskrivelse:\"}]}]},{\"linkId\":\"5d4c482f-224c-4fe1-9083-23bf8623d43c\",\"text\":\"Har du, eller har du hatt, problemer med stoffskifte?\",\"answer\":[{\"item\":[{\"linkId\":\"048ea633-71c7-44e5-9a87-89bac091b928\",\"text\":\"Beskrivelse:\"}]}]},{\"linkId\":\"6dd031a1-57dc-4ce6-dbb6-61dc0389076b\",\"text\":\"Har du leddgikt eller muskel-og skjelettsykdom?\",\"answer\":[{\"item\":[{\"linkId\":\"7eb077be-6b66-49a1-ffbf-2f511b86c0b3\",\"text\":\"Beskrivelse:\"}]}]},{\"linkId\":\"346b594c-349f-43d2-83fb-35697ffde5c6\",\"text\":\"Har du, eller har du hatt, problemer med mage eller tarm?\",\"answer\":[{\"item\":[{\"linkId\":\"7de135a6-94de-4389-940c-d7abb04a7739\",\"text\":\"Er du plaget med magesÃ¥r, magekatarr, spiserÃ¸rsbrokk eller sure oppstÃ¸t?\"},{\"linkId\":\"e4e4d5be-1c9c-4bd6-8af3-3f9f48d77d28\",\"text\":\"Beskrivelse av problemer med mage eller tarm:\"}]}]},{\"linkId\":\"c830e721-3f40-4c3d-809b-80c37a98a7dd\",\"text\":\"Har du, eller har du hatt, psykiske sykdommer?\",\"answer\":[{\"item\":[{\"linkId\":\"e98a06ac-8f54-4af6-821a-099bb494c647\",\"text\":\"Beskrivelse av psykisk sykdom\"}]}]},{\"linkId\":\"3692189c-aecc-4bb2-931d-d89528ada4ff\",\"text\":\"Har du, eller har du hatt, rusavhengighet (inkl. alkoholavhengighet)?\",\"answer\":[{\"item\":[{\"linkId\":\"ccf5c7ad-93c6-476b-b90a-ae02fc6c130a\",\"text\":\"Hvilken type rusavhengighet?\"}]}]}]}]},{\"linkId\":\"9f784d4d-132e-4fff-84b2-efb3ec015d76\",\"text\":\"FÃ¥r du brystsmerter eller blir du tungpustet nÃ¥r du gÃ¥r opp trapper i normalt tempo?\"},{\"linkId\":\"c0b063ac-2f32-4789-812b-d189b3b393a2\",\"text\":\"Har du smittsom sykdom?\",\"answer\":[{\"item\":[{\"linkId\":\"9030d990-c740-47ee-e3b2-96bd099e9e46\"},{\"linkId\":\"d292ea76-ea9b-40b1-8037-4d02d231adb9\",\"text\":\"Hvilken?\"}]}]},{\"linkId\":\"4c41b9ac-3bb2-4894-ad78-77bef263cde5\",\"text\":\"Har du annen helseinformasjon eller  sykdom som vi bÃ¸r vite om?\",\"answer\":[{\"item\":[{\"linkId\":\"2d7f03a9-e95e-4580-8f09-1ca64f5aa8b1\",\"text\":\"Beskrivelse:\"}]}]}]},{\"linkId\":\"452dca3f-953a-40a4-88ea-3427994d4b33\",\"text\":\"Medisiner\",\"item\":[{\"linkId\":\"f355b57e-8e90-4602-8602-3addb5a96c70\",\"text\":\"Bruker du noen faste medisiner?\",\"answer\":[{\"item\":[{\"linkId\":\"299a0cc3-550d-498e-9696-6c4b15959abe\",\"text\":\"Velg det som gjelder for deg:\"},{\"linkId\":\"17148380-4a65-4b7e-8f75-eeb127d1e78b\",\"text\":\"Jeg bruker blodfortynnende medisiner\",\"answer\":[{\"valueBoolean\":false,\"item\":[{\"linkId\":\"326328c8-8e0d-4d65-f7f3-b0d8945b46f2\",\"text\":\"Vennligst spesifiser hvilke blodfortynnende medisiner du bruker, dosering og hvorfor du bruker disse\"}]}]},{\"linkId\":\"5fc528b5-a673-4861-8913-7946084e6039\",\"text\":\"Jeg bruker andre reseptbelagte medisiner. Gjelder ogsÃ¥ prevensjonsmidler.\",\"answer\":[{\"valueBoolean\":false,\"item\":[{\"linkId\":\"58a9a828-4a69-4c8d-98ef-9abed1c4c8e5\",\"text\":\"Vennligst spesifiser hvilke andre reseptbelagte medisiner (inkl. prevensjon) du bruker, dosering og hvorfor du bruker disse\"}]}]},{\"linkId\":\"0f18b69b-3c33-4170-8781-4cd836dbd235\",\"text\":\"Jeg bruker reseptfrie medisiner, kosttilskudd eller naturmedisin\",\"answer\":[{\"valueBoolean\":false,\"item\":[{\"linkId\":\"420a25e1-2002-4143-fc85-3f57a101d06f\",\"text\":\"Vennligst spesifiser hvilke reseptfrie medisiner, kosttilskudd eller naturmedisin du bruker, dosering, og hvorfor du bruker disse\"}]}]}]}]}]},{\"linkId\":\"1625a991-900b-4313-e4e2-faf44b2c70b8\",\"text\":\"Er du allergisk mot noen typer antibiotika eller andre medisiner?\",\"answer\":[{\"item\":[{\"linkId\":\"239c5a75-59e0-43b2-9ad0-f5ca56cef5bc\",\"text\":\"Beskriv hvilke medisiner du er allergisk mot og hvordan du reagerer\"}]}]},{\"linkId\":\"8b72c88b-8834-4a37-889e-211ccabfc6f6\",\"text\":\"Mat, livsstil og dagligliv\",\"item\":[{\"linkId\":\"3c441130-ef2d-422d-a4b5-ffaa3260c82c\",\"text\":\"Har du behov for spesiell kost?\",\"answer\":[{\"item\":[{\"linkId\":\"aa5cba6a-f45c-4b74-8042-35155f8790de\",\"text\":\"Beskriv:\"},{\"linkId\":\"f3a40c58-788e-4c1d-867b-d5545287a6c2\"}]}]},{\"linkId\":\"4c58e45b-b1ca-42f6-8706-af0282aa60eb\",\"text\":\"Er du allergisk mot matvarer, pollen, lateks, nikkel eller annet?\",\"answer\":[{\"item\":[{\"linkId\":\"6d6c1fe9-e684-41ed-c218-850c1533535b\",\"text\":\"Hva er du allergisk mot?\",\"answer\":[{\"item\":[{\"linkId\":\"90f6d5dc-69b6-42a0-eead-4fdac6b877c2\",\"text\":\"Hvordan reagerer du?\"}]}]}]}]},{\"linkId\":\"f82099b5-342e-48d4-9e5b-7c4d821527fc\",\"text\":\"Klarer du Ã¥ dusje, kle pÃ¥ deg og utfÃ¸re daglige gjÃ¸remÃ¥l selv?\",\"answer\":[{\"item\":[{\"linkId\":\"fea89ddd-b269-4ffb-e46d-b503c47de9fc\",\"text\":\"Hva trenger du hjelp til?\"}]}]},{\"linkId\":\"fe604a22-db61-4231-b5f0-d5f42e0fd549\",\"text\":\"Har du problemer med syn, hÃ¸rsel eller Ã¥ snakke?\",\"answer\":[{\"item\":[{\"linkId\":\"3e21cd5c-29f6-42a2-992c-437e53a20efc\",\"text\":\"Beskriv:\"}]}]},{\"linkId\":\"0701946f-d908-4a03-fe32-6a1da2b43605\",\"text\":\"RÃ¸yker du?\",\"answer\":[{\"item\":[{\"linkId\":\"90552c7d-e531-4b98-884e-088ca908bf10\",\"text\":\"Hvor ofte rÃ¸yker du?\"},{\"linkId\":\"c11f1dbc-4f28-4adc-9c48-65a16ad793c2\",\"text\":\"Har du rÃ¸kt tidligere?\",\"answer\":[{\"item\":[{\"linkId\":\"0970e4ec-2b80-46cb-81ea-f6a555352497\",\"text\":\"NÃ¥r sluttet du Ã¥ rÃ¸yke?\"}]}]}]}]},{\"linkId\":\"fc7e0b2e-a7bc-433c-981d-b38086c10ee6\",\"text\":\"Bruker du snus?\",\"answer\":[{\"item\":[{\"linkId\":\"8ab13c89-6bd1-499b-b1ac-2f13bef3f00c\",\"text\":\"Hvor ofte bruker du snus?\"},{\"linkId\":\"6e20e9b9-b2e8-47bf-8549-685dccc3dcab\",\"text\":\"Har du brukt snus tidligere?\",\"answer\":[{\"item\":[{\"linkId\":\"3b0fa229-bd3e-4f27-8e12-efdcbd870bee\",\"text\":\"NÃ¥r sluttet du med snus?\"}]}]}]}]},{\"linkId\":\"5d53fa65-6884-47ab-e871-558bd493fd46\",\"text\":\"Drikker du alkohol?\",\"answer\":[{\"item\":[{\"linkId\":\"826275ff-9d1d-4501-fb68-fb6a178c904c\",\"text\":\"Hvor ofte drikker du alkohol?\"}]}]},{\"linkId\":\"d12c0be8-8940-44ff-ce8b-c8237f207fff\",\"text\":\"Bruker du andre rusmidler?\",\"answer\":[{\"item\":[{\"linkId\":\"4c0e0841-bd2e-43e9-882d-8e0c60fe0fc8\"}]}]}]},{\"linkId\":\"f8ea6cbe-d6d4-45dc-bc6b-57a9acc81e1c\",\"text\":\"HÃ¸yde og vekt\",\"item\":[{\"linkId\":\"b0e32358-a0f9-421e-c03d-6f88cc3c05ea\",\"text\":\"HÃ¸yde i centimeter\"},{\"linkId\":\"1300523a-1934-4c5c-ace0-e741cdd119ea\",\"text\":\"Vekt i kilo\",\"answer\":[{\"item\":[{\"linkId\":\"fa2dff09-af57-489c-a816-40f698f80d79\"}]}]}]},{\"linkId\":\"30981d2a-3426-462f-e37f-e1279541c724\",\"text\":\"## Hjelp oss Ã¥ forberede din operasjon\",\"item\":[{\"linkId\":\"e80c779e-28d9-48a0-8124-2ff136d7ec67\",\"text\":\"Dersom du skal opereres, er det noen flere spÃ¸rsmÃ¥l vi Ã¸nsker at du svarer pÃ¥. Vi ber deg svare pÃ¥ disse spÃ¸rsmÃ¥lene selv om det ikke er helt sikkert at du skal opereres.\"},{\"linkId\":\"1553458c-b271-44ed-c47d-e1b30d270076\",\"text\":\"Skal du opereres eller undersÃ¸kes i narkose?\",\"answer\":[{\"item\":[{\"linkId\":\"007b05b2-f294-48f4-d313-4e4857cca7b0\",\"text\":\"Velg:\"},{\"linkId\":\"50040ff6-ac11-4f6d-b5eb-fc3a1bb63c19\",\"text\":\"Har du blitt operert tidligere?\",\"answer\":[{\"item\":[{\"linkId\":\"08f0fe85-05f8-42b0-fffd-400511211716\",\"text\":\"Har du tatt keisernsnitt?\"},{\"linkId\":\"5900eecd-58b6-4bde-e341-d4d0d69c5042\",\"text\":\"Fikk du problemer (komplikasjoner) etter operasjonen?\",\"answer\":[{\"item\":[{\"linkId\":\"8849ca88-9418-4c18-8b57-15a00cedb0e1\",\"text\":\"Beskrivelse:\"}]}]},{\"linkId\":\"74e49301-baf2-4e18-882f-44a843fa3d32\",\"text\":\"Har du fÃ¥tt narkose eller lokalbedÃ¸velse tidligere?\"},{\"linkId\":\"7a34ee84-f732-457b-e4d2-bbe34f85dbf6\",\"text\":\"Har du, eller noen du er i slekt med, reagert unormalt pÃ¥ narkose eller bedÃ¸velse?\",\"answer\":[{\"item\":[{\"linkId\":\"1705552a-1f5b-4c68-85e4-aef963ff4d08\"},{\"linkId\":\"dbe73920-e8be-46e5-cdd2-7eb5a3227215\",\"text\":\"Hva slags bedÃ¸velse og hva slags reaksjon?\"}]}]},{\"linkId\":\"d4d0b28d-2ceb-44df-8867-20345d4c823b\",\"text\":\"Har du lett for Ã¥ bli kvalm eller reisesyk?\"},{\"linkId\":\"ed87e387-4499-47c4-88b5-652718f5a0b7\",\"text\":\"Har du hatt vekttap de siste ukene?\",\"answer\":[{\"item\":[{\"linkId\":\"5c47b63d-5f57-40ee-85d9-23db41dba0c5\",\"text\":\"Har du spist mindre enn normalt de siste ukene?\"},{\"linkId\":\"90ae47ba-124a-433d-e988-82a59135f196\",\"text\":\"Beskriv vekttap og eventuelt redusert matinntak:\"}]}]}]}]},{\"linkId\":\"e163d107-bfa1-4633-e35d-30a1755102e9\",\"text\":\"Har du problemer med Ã¥ bÃ¸ye hodet bakover?\",\"answer\":[{\"item\":[{\"linkId\":\"4cbd99f4-f94c-47c5-8b70-b6a67cabd48f\",\"text\":\"Beskrivelse:\"}]}]},{\"linkId\":\"59f9f96e-1345-435c-8a57-846d432cf364\",\"text\":\"Har du problemer med Ã¥ bevege kjeven eller har du nedsatt gapeevne (mindre enn ca 3 cm)?\",\"answer\":[{\"item\":[{\"linkId\":\"8eb5a361-6d6a-4450-8fba-558d3cd5f16b\",\"text\":\"Beskrivelse:\"}]}]},{\"linkId\":\"8bf9e5ae-b4a0-484b-a9b1-c752c05ae1d4\",\"text\":\"Har du problemer med Ã¥ bevege kjeven eller Ã¥pne munnen (nedsatt gapeevne)?\",\"answer\":[{\"item\":[{\"linkId\":\"73eb238f-b9a9-4253-8397-f45451e9a9b9\",\"text\":\"Beskrivelse:\"}]}]},{\"linkId\":\"37087725-8bad-4ea2-bf5e-eff76092105c\",\"text\":\"Klarer du Ã¥ ligge flatt pÃ¥ ryggen?\",\"answer\":[{\"item\":[{\"linkId\":\"2a734839-28a2-4803-871c-f25a57f7998f\",\"text\":\"Beskrivelse:\"}]}]},{\"linkId\":\"96fdbbe2-d0aa-4eef-8207-685925681b8f\",\"text\":\"Har du lÃ¸se tenner, tannprotese eller piercing i munnen?\",\"answer\":[{\"item\":[{\"linkId\":\"7338e063-c11e-4cca-99a1-288664011e76\",\"text\":\"Beskrivelse:\"},{\"linkId\":\"315fee85-bb56-4237-f642-36c1ca2b03b4\"}]}]},{\"linkId\":\"05321c5f-86e6-4ae9-bd6d-2418d64ea413\",\"text\":\"Har du sÃ¥r eller utslett pÃ¥ i huden der du skal opereres?\",\"answer\":[{\"item\":[{\"linkId\":\"08c967bf-f950-4981-d0be-76996e0847f4\",\"text\":\"Beskrivelse:\"}]}]}]}]}]},{\"linkId\":\"e83c2701-19ce-43eb-b5b2-6cbc1c831d65\",\"text\":\"Er det andre forhold vi trenger Ã¥ vite om i forbindelse med undersÃ¸kelse/behandling?\",\"answer\":[{\"item\":[{\"linkId\":\"266498d6-e1cc-445a-8111-891f6d7420d9\",\"text\":\"Beskrivelse:\"}]}]},{\"linkId\":\"c94140c4-67eb-4bc2-8ae1-bccd40000977\",\"text\":\"Hvilket sykehus har du avtale pÃ¥?\"},{\"linkId\":\"113ec519-efbb-4ae4-8164-bd31ec7f3d3e\",\"text\":\"Oppsummering\",\"item\":[{\"linkId\":\"43c43391-f469-456a-e479-9c5a0a390498\",\"text\":\"Pasienten trenger tolk\"},{\"linkId\":\"f92ba068-5581-4b6e-864d-11318aee1d22\",\"text\":\"Pasienten har barn eller sÃ¸sken under 18 Ã¥r\"},{\"linkId\":\"1a552314-c6d1-4b73-907a-d14d5391a5b2\",\"text\":\"Pasienten er gravid\"},{\"linkId\":\"d675de57-d33f-4757-8086-6d2c58f3020f\",\"text\":\"Pasienten er usikker pÃ¥ om hun er gravid\"},{\"linkId\":\"74cfd3a7-068f-4828-8a3a-aeeaa52c6061\",\"text\":\"Pasienten ammer\"},{\"linkId\":\"cfe312a8-ce26-4a66-bc56-c641d1742877\",\"text\":\"Pasienten angir Ã¥ ha (eller ha hatt) hjerte- karsykdom\"},{\"linkId\":\"fae5d05a-d471-49f6-896e-fd3d8a319066\",\"text\":\"Det er usikkert om pasienten har (eller har hatt) hjerte- karsykdom\"},{\"linkId\":\"f2b5c8d8-386f-46a1-e2cf-a7649ec28445\",\"text\":\"Pasienten angir Ã¥ ha (eller ha hatt) hypertensjon\"},{\"linkId\":\"71f87975-d9df-4c68-b6cd-61c6d08546dc\",\"text\":\"Det er usikker om pasienten har (eller har hatt) hypertensjon\"},{\"linkId\":\"2a009f8e-42a7-4ad1-9fcb-8521531d612b\",\"text\":\"Pasienten angir Ã¥ ha (eller ha hatt) Ã¸kt blÃ¸dningstendens\"},{\"linkId\":\"cd56d85e-0f1c-475a-8381-febc4aeac4c8\",\"text\":\"Det er usikkert om pasienten har (eller har hatt) Ã¸kt blÃ¸dningstendens\"},{\"linkId\":\"2b4c5c4f-6b70-4c4e-ac5b-eb0a74a0bf7c\",\"text\":\"Pasienten angir Ã¥ ha (eller ha hatt) lungesykdom\"},{\"linkId\":\"1e2c8278-3e41-4f8f-8b6c-7d0cf685b00a\",\"text\":\"Det er usikkert om pasienten har (eller har hatt) lungesykdom\"},{\"linkId\":\"1c71fc14-521f-4f68-86b4-414f19d2c371\",\"text\":\"Pasienten angir Ã¥ ha (eller ha hatt) nevrologisk sykdom\"},{\"linkId\":\"5bc60ddd-0192-46ff-e50c-985435e4535f\",\"text\":\"Det er usikkert om pasienten har (eller har hatt) nevrologisk sykdom\"},{\"linkId\":\"3955f5f3-835c-4241-a54d-f5544d5b8e8c\",\"text\":\"Pasienten angir Ã¥ ha (eller ha hatt) nyresykdom\"},{\"linkId\":\"24d4491a-0df6-4b1a-83f7-338fa957ea39\",\"text\":\"Det er usikkert om pasienten har (eller har hatt) nyresykdom\"},{\"linkId\":\"5a8edeed-5df4-4970-84c3-021a4d412574\",\"text\":\"Pasienten angir Ã¥ ha (eller ha hatt) leversykdom\"},{\"linkId\":\"c51c1222-66a9-47c3-c303-83a42195801c\",\"text\":\"Det er usikkert om pasienten har (eller har hatt) leversykdom\"},{\"linkId\":\"42ae62d1-274c-4851-d3f5-64b8569691d7\",\"text\":\"Pasienten angir Ã¥ ha (eller ha hatt) diabetes\"},{\"linkId\":\"81736232-378d-4eee-8d55-0bbba3d5d901\",\"text\":\"Det er usikkert om pasienten har (eller har hatt) diabetes\"},{\"linkId\":\"a67896c0-9c85-4d8b-93c1-dd7fb2d9bddb\",\"text\":\"Pasienten angir Ã¥ ha (eller ha hatt) problemer med stoffskifte\"},{\"linkId\":\"a2bf65a1-b1f0-4abd-9ba3-198abada4bad\",\"text\":\"Det er usikkert om pasienten har (eller har hatt) problemer med stoffskifte\"},{\"linkId\":\"34655387-0ecc-4ea5-cd20-d2c0b0d1e09b\",\"text\":\"Pasienten angir Ã¥ ha (eller ha hatt) leddgikt eller muskel- og skjelettsykdom\"},{\"linkId\":\"d57de31c-6d85-41e3-81de-cb8d39d91a68\",\"text\":\"Det er usikkert om pasienten har (eller har hatt) leddgikt eller muskel- og skjelettsykdom\"},{\"linkId\":\"bf630305-f881-4d36-8efe-9922f8e85c2b\",\"text\":\"Pasienten angir Ã¥ ha (eller ha hatt) problemer med mage/tarm\"},{\"linkId\":\"cba8c917-79b8-40c9-86ec-eea42cc5f9ac\",\"text\":\"Det er usikkert om pasienten har (eller har hatt) problemer med mage/tarm\"},{\"linkId\":\"b1bc91d4-c3a2-491f-8cee-8086c23a1130\",\"text\":\"Pasienten angir Ã¥ ha (eller ha hatt) problemer med mage/tarm\"},{\"linkId\":\"a0158be3-8ffe-4d72-e78c-4e751f000a8f\",\"text\":\"Det er usikkert om pasienten har (eller har hatt) problemer med mage/tarm\"},{\"linkId\":\"b0755346-fb58-4ed2-8ece-41be3bd3a947\",\"text\":\"Pasienten angir Ã¥ vÃ¦re plaget med magesÃ¥r, magekatarr, spiserÃ¸rsbrokk eller sure oppstÃ¸t\"},{\"linkId\":\"81df1a07-a27b-4f31-92b1-7298cb73c5b2\",\"text\":\"Pasienten angir Ã¥ ha (eller ha hatt) problemer med mage/tarm\"},{\"linkId\":\"3fc2dd89-2173-439d-8d15-b24a9bc9ee4a\",\"text\":\"Pasienten angir Ã¥ ha (eller ha hatt) psykisk sykdom\"},{\"linkId\":\"c2b3944b-a7ad-4ded-8fd8-f9d1f97b5e65\",\"text\":\"Det er usikkert om pasienten har (eller har hatt) psykisk sykdom\"},{\"linkId\":\"4cbeda65-3a3a-4954-806a-47a55e2ac126\",\"text\":\"Pasienten angir Ã¥ ha (eller ha hatt) rusavhengighet (inkl. alkoholavhengighet)\"},{\"linkId\":\"215a79d9-e136-462a-9e9f-f26c75f7f83d\",\"text\":\"Det er usikkert om pasienten har (eller ha hatt) rusavhengighet (inkl. alkoholavhengighet)\"},{\"linkId\":\"c268192a-7c55-4c56-849a-081253173a1a\",\"text\":\"Pasienten angir Ã¥ fÃ¥ brystsmerter ved trappegange\"},{\"linkId\":\"bd9d68f6-0c7c-42fa-86ae-da304875385b\",\"text\":\"Pasienten angir Ã¥ ha en smittsom sykdom\"},{\"linkId\":\"b6d10bfd-fa3e-46ec-e014-56c84f6ce6bd\",\"text\":\"Pasienten er usikker pÃ¥ om det foreligger smittsom sykdom\"},{\"linkId\":\"d4d4ef20-8d3c-44cc-86f6-9104bfc17494\",\"text\":\"Pasienten bruker blodfortynnende medisiner\"},{\"linkId\":\"ef7621f7-8e79-41ae-8d52-336931d6cd19\",\"text\":\"Pasienten bruker reseptbelagte medisiner\"},{\"linkId\":\"d9a83ec5-363e-4a8a-a03b-dd2c84850583\",\"text\":\"Pasienten bruker reseptfrie medisiner, kosttilskudd eller naturmedisin\"},{\"linkId\":\"5eccd6e2-d9eb-4e50-8a87-f17c37aafb3f\",\"text\":\"Pasienten angir behov for spesialkost\"},{\"linkId\":\"0b0e000d-3b61-49a9-9760-c1c0a50142ca\",\"text\":\"Pasienten angir allergi for matvarer, pollen, latex, nikkel eller annet\"},{\"linkId\":\"2dde5c11-47b3-4fd7-8a63-cad49206b068\",\"text\":\"Pasienten angir usikker allergi for matvarer, pollen, latex, nikkel eller annet\"},{\"linkId\":\"695ecd5a-93b7-478f-8d29-629a30f9bdc2\",\"text\":\"Pasienten klarer ikke dusje, kle pÃ¥ seg, eller utfÃ¸re daglige gjÃ¸remÃ¥l\"},{\"linkId\":\"e9fefc0d-ee22-4581-8adf-8f860fa3de6f\",\"text\":\"Pasienten har problemer med syn, hÃ¸rsel eller Ã¥ snakke\"},{\"linkId\":\"a1ce7612-9d26-4351-bd24-1648167d784d\",\"text\":\"Pasienten drikker alkohol daglig\"},{\"linkId\":\"8606e192-c103-4f54-8eff-2a94e4fa3e96\",\"text\":\"Pasienten rÃ¸yker\"},{\"linkId\":\"88fe2359-0d6b-498a-c2fb-37383e7a3a31\",\"text\":\"Pasienten har rÃ¸kt tidligere\"},{\"linkId\":\"f170b256-fa0b-4569-8ee0-92c8ca33161c\",\"text\":\"Pasienten har brukt snus tidligere\"},{\"linkId\":\"e10a4d80-43b1-4de7-8754-71280744f272\",\"text\":\"Pasienten bruker rusmidler\"},{\"linkId\":\"bc697f2c-be82-41bd-a755-600932641967\",\"text\":\"Pasienten har gjennomgÃ¥tt operasjon tidligere\"},{\"linkId\":\"c5528f93-928f-40b3-89b2-fd54c38fac78\",\"text\":\"Pasienten har tatt keisernsitt\"},{\"linkId\":\"8f4a79a6-4af9-4ded-ea3a-34106aebcb55\",\"text\":\"Pasienten angir komplikasjoner etter operasjon\"},{\"linkId\":\"940b91d2-3bf3-4b22-b52c-f1381320ca92\",\"text\":\"Pasienten angir Ã¥ vÃ¦re usikker pÃ¥ om det var komplikasjoner etter operasjon\"},{\"linkId\":\"3e1eec4c-9340-4966-9f93-0d4daaf0465d\",\"text\":\"Pasienten har fÃ¥tt narkose eller lokalbedÃ¸velse tidligere\"},{\"linkId\":\"38994d70-c59f-4d6c-8378-85fbb9297a07\",\"text\":\"Pasienten angir at hen selv eller noen i slekten har reagert unormalt pÃ¥ narkose eller bedÃ¸velse\"},{\"linkId\":\"c8633872-83e9-4f6c-ad0d-d50748b06715\",\"text\":\"Pasienten angir Ã¥ lett bli kvalm eller reisesyk\"},{\"linkId\":\"f72a30b6-bbc0-41ef-95f6-aed85f542162\",\"text\":\"Pasienten vet ikke om hen lett blir kvalm eller reisesyk\"},{\"linkId\":\"8390ac0e-a8b9-48fe-a987-c636c198594a\",\"text\":\"Pasienten angir vekttap siste ukene\"},{\"linkId\":\"69c974e9-4d1a-473d-8beb-1530bb27624f\",\"text\":\"Pasienten er usikker pÃ¥ vekttap siste ukene\"},{\"linkId\":\"7b7c8d28-f2be-4cbd-fe61-95b7cb4c280d\",\"text\":\"Pasienten angir problemer med Ã¥ bÃ¸ye hodet bakover\"},{\"linkId\":\"3046cb1b-ecb4-4858-8cb8-031ac78a7ce3\",\"text\":\"Pasienten vet ikke om det er problemer med Ã¥ bÃ¸ye hodet bakover\"},{\"linkId\":\"490dbb65-a2cf-4c1c-e16a-d5fe617958ec\",\"text\":\"Pasienten har problemer med Ã¥ bevege i kjeven eller nedsatt gapeevne\"},{\"linkId\":\"6b4af00c-706c-4081-8dfa-d568d213f9ca\",\"text\":\"Pasienten har problemer med Ã¥ bevege i kjeven eller nedsatt gapeevne\"},{\"linkId\":\"950c23e3-3735-4bea-8386-fd4b0ec04b04\",\"text\":\"Pasienten vet ikke om det er problemer med Ã¥ bevege i kjeven eller om det er nedsatt gapeevne\"},{\"linkId\":\"21c9645b-6c15-4699-8b43-c1a92e74ab2b\",\"text\":\"Pasienten vet ikke om det er problemer med Ã¥ bevege i kjeven eller om det er nedsatt gapeevne\"},{\"linkId\":\"75b4476d-77bb-41df-9831-d7de4a1b2fbe\",\"text\":\"Pasienten angir Ã¥ ikke kunne ligge flatt pÃ¥ ryggen\"},{\"linkId\":\"1fe594df-9adc-40f4-8fd0-fe34e4f185e2\",\"text\":\"Pasienten vet ikke om det er mulig Ã¥ ligge flatt pÃ¥ ryggen\"},{\"linkId\":\"cc436458-8579-495a-e3b1-af534b2b00c4\",\"text\":\"Pasienten har lÃ¸se tenner, tannprotese eller piercing i munnen\"},{\"linkId\":\"9e8bb134-ea0a-468e-889c-7af70d5af887\",\"text\":\"Pasienten angir sÃ¥r eller utslett i huden i operasjonsomrÃ¥det\"},{\"linkId\":\"3ce56812-ef33-498e-c236-cceafc1eafa8\",\"text\":\"Pasienten vet ikke om det er sÃ¥r eller utslett i huden i operasjonsomrÃ¥det\"}]},{\"linkId\":\"6dc39574-5a82-4e7c-80ec-fe190e7e6c6f\"},{\"linkId\":\"ffd01292-84c2-423b-81bf-623cf693d9fb\"},{\"linkId\":\"e59ebb90-9fa2-4421-888d-3d6087dfa09f\"}]}"}"""
        val expected = """{
            |    "DokumentGuid": "e88
        """.trimMargin()

        assertEquals(expected, SimpleJsonGrammar.parseToEnd(input).toPlainString().substring(0, 26))
    }

    @Test
    fun testParse5() {
        val input = "\"asdf\""
        assertEquals(input, SimpleJsonGrammar.parseToEnd(input).toPlainString())
    }

    @Test
    fun testParse6() {
        val input = "\"test \\\" test \""
        assertEquals(input, SimpleJsonGrammar.parseToEnd(input).toPlainString())
    }
}