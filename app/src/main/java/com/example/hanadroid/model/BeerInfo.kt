package com.example.hanadroid.model

import com.google.gson.annotations.SerializedName

/**
 * {
 *         "id": 1,
 *         "name": "Buzz",
 *         "tagline": "A Real Bitter Experience.",
 *         "first_brewed": "09/2007",
 *         "description": "A light, crisp and bitter IPA brewed with English and American hops. A small batch brewed only once.",
 *         "image_url": "https://images.punkapi.com/v2/keg.png",
 *         "abv": 4.5,
 *         "ibu": 60,
 *         "target_fg": 1010,
 *         "target_og": 1044,
 *         "ebc": 20,
 *         "srm": 10,
 *         "ph": 4.4,
 *         "attenuation_level": 75,
 *         "volume": {
 *             "value": 20,
 *             "unit": "litres"
 *         },
 *         "boil_volume": {
 *             "value": 25,
 *             "unit": "litres"
 *         },
 *         "method": {
 *             "mash_temp": [
 *                 {
 *                     "temp": {
 *                         "value": 64,
 *                         "unit": "celsius"
 *                     },
 *                     "duration": 75
 *                 }
 *             ],
 *             "fermentation": {
 *                 "temp": {
 *                     "value": 19,
 *                     "unit": "celsius"
 *                 }
 *             },
 *             "twist": null
 *         },
 *         "ingredients": {
 *             "malt": [
 *                 {
 *                     "name": "Maris Otter Extra Pale",
 *                     "amount": {
 *                         "value": 3.3,
 *                         "unit": "kilograms"
 *                     }
 *                 },
 *                 {
 *                     "name": "Caramalt",
 *                     "amount": {
 *                         "value": 0.2,
 *                         "unit": "kilograms"
 *                     }
 *                 },
 *                 {
 *                     "name": "Munich",
 *                     "amount": {
 *                         "value": 0.4,
 *                         "unit": "kilograms"
 *                     }
 *                 }
 *             ],
 *             "hops": [
 *                 {
 *                     "name": "Fuggles",
 *                     "amount": {
 *                         "value": 25,
 *                         "unit": "grams"
 *                     },
 *                     "add": "start",
 *                     "attribute": "bitter"
 *                 },
 *                 {
 *                     "name": "First Gold",
 *                     "amount": {
 *                         "value": 25,
 *                         "unit": "grams"
 *                     },
 *                     "add": "start",
 *                     "attribute": "bitter"
 *                 },
 *                 {
 *                     "name": "Fuggles",
 *                     "amount": {
 *                         "value": 37.5,
 *                         "unit": "grams"
 *                     },
 *                     "add": "middle",
 *                     "attribute": "flavour"
 *                 },
 *                 {
 *                     "name": "First Gold",
 *                     "amount": {
 *                         "value": 37.5,
 *                         "unit": "grams"
 *                     },
 *                     "add": "middle",
 *                     "attribute": "flavour"
 *                 },
 *                 {
 *                     "name": "Cascade",
 *                     "amount": {
 *                         "value": 37.5,
 *                         "unit": "grams"
 *                     },
 *                     "add": "end",
 *                     "attribute": "flavour"
 *                 }
 *             ],
 *             "yeast": "Wyeast 1056 - American Ale™"
 *         },
 *         "food_pairing": [
 *             "Spicy chicken tikka masala",
 *             "Grilled chicken quesadilla",
 *             "Caramel toffee cake"
 *         ],
 *         "brewers_tips": "The earthy and floral aromas from the hops can be overpowering. Drop a little Cascade in at the end of the boil to lift the profile with a bit of citrus.",
 *         "contributed_by": "Sam Mason <samjbmason>"
 *     }
 */
data class BeerInfo(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("tagline")
    val tagline: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("image_url")
    val imageUrl: String = "",
    @SerializedName("brewers_tips")
    val brewerTips: String = ""
)
