package com.example.top10downloaderapp

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

class XMLParser {
    private val ns: String? = null

    fun parse(inputStream: InputStream): ArrayList<Details> {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
            parser.nextTag()
            return readQuestionsRssFeed(parser)
        }
    }

    private fun readQuestionsRssFeed(parser: XmlPullParser): ArrayList<Details> {

        val qDetails = ArrayList<Details>()

        parser.require(XmlPullParser.START_TAG, ns, "feed")

        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            if (parser.name == "entry") {
                parser.require(XmlPullParser.START_TAG, ns, "entry")
                var title: String? = null
                var summary: String? = null
                var ID:String?=null
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.eventType != XmlPullParser.START_TAG) {
                        continue
                    }
                    when (parser.name) {
                        "id" -> ID = readTag(parser,"id")
                        "title" -> title = readTag(parser,"title")
                        "summary" -> summary = readTag(parser,"summary")
                        else -> skip(parser)
                    }
                }
                qDetails.add(Details(ID,title,summary))
            } else {
                skip(parser)
            }
        }
        return qDetails
    }

    private fun readTag(parser: XmlPullParser, tag:String): String {
        parser.require(XmlPullParser.START_TAG, ns, tag)
        val myTag = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, tag)
        return myTag
    }


    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}