package com.example.WealthMan;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.Collection;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;

public class APIInterface {

    public class Quote{
        public String symbol = null;
        public String companyName = null;
        public String primaryExchange = null;
        public String sector = null;
        public String calculationPrice = null;
        public float open = 0;
        public long openTime = 0;              // Epoch time
        public float close = 0;
        public long closeTime = 0;
        public float high = 0;
        public float low = 0;
        public float latestPrice = 0;
        public String latestSource = null;
        public String latestTime = null;
        public long latestUpdate= 0;           // Epoch time
        public long latestVolume = 0;
        public float iexRealtimePrice = 0;
        public long iexRealtimeSize = 0;
        public long  iexLastUpdated = 0;       // Epoch time
        public float delayedPrice = 0;
        public long  delayedPriceTime = 0;              // Epoch time
        public float extendedPrice = 0;
        public float extendedChange = 0;
        public float extendedChangePercent = 0;
        public long extendedPriceTime = 0;              // Epoch time
        public float previousClose = 0;
        public float change = 0;
        public float changePercent = 0;
        public float iexMarketPercent = 0;
        public long  iexVolume = 0;
        public long  avgTotalVolume = 0;
        public float iexBidPrice = 0;
        public long  iexBidSize = 0;
        public float iexAskPrice = 0;
        public long  iexAskSize = 0;
        public long  marketCap = 0;
        public float peRatio = 0;
        public float week52High = 0;
        public float week52Low = 0;
        public float ytdChange = 0;
        public String getSymbol() {
            return symbol;
        }
    }
    public class article {
        public String datetime = null;
        public String headline = null;
        public String source = null;
        public String url = null;
        public String summary = null;
        public String related = null;
        public String imageUrl = null;
    }
    public class chartDatum{
        public String date = null; //": "2017-04-03",
        public String open = null; //": 143.1192,
        public float high = 0; //": 143.5275,
        public float low = 0; //": 142.4619,
        public float close = 0;//": 143.1092,
        public long volume = 0; //": 19985714,
        public float unadjustedClose = 0; //": 143.7,
        public long unadjustedVolume = 0; //": 19985714,
        public float change = 0; //": 0.039835,
        public float changePercent = 0; //": 0.028,
        public float vwap = 0; //": 143.0507,
        public String label = null; //": "Apr 03, 17",
        public float changeOverTime = 0; //": -0.0039
    }
    public class stockSym{
        String symbol;
        String name;
        String date;
        boolean isEnabled;
        String type;
        int iexId;
    }
    public static class QuoteList {
        List<Quote> quotes;
        public QuoteList(List<Quote> quotes) {
            this.quotes = quotes;
        }
    }
    public class Batch{
        public Quote quote;
        public ArrayList<article> news;
        public ArrayList<chartDatum> chart;
    }
    public static class Batches {
        List<Batch> batches;
        public Batches(List<Batch> batches) {
            this.batches = batches;
        }
        public int size(){
            return batches.size();
        }

    }
    public static class CompanyListDeserializer implements JsonDeserializer<Batches> {
        @Override
        public Batches deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = element.getAsJsonObject();
            List<Batch> batches = new ArrayList<Batch>();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                Batch batch = context.deserialize(entry.getValue(), Batch.class);
                batches.add(batch);
            }
            return new Batches(batches);
        }
    }
    public static class watchListData{
        private String symbol;
        private String name;
        private Float price;
        private Float change;

        public void setPrice(Float price){
            this.price = price;
        }
        public void setChange(Float change){
            this.change = change;
        }
        public void setSymbol(String symbol){
            this.symbol = symbol;
        }
        public void setName(String name){
            this.name = name;
        }

        public Float getPrice(){
            return price;
        }
        public Float getChange(){
            return change;
        }
        public String getSymbol(){
            return symbol;
        }
        public String getName(){
            return name;
        }
    }
    public static class QuoteListDeserializer implements JsonDeserializer<QuoteList> {

        @Override
        public QuoteList deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = element.getAsJsonObject();
            List<Quote> quotes = new ArrayList<Quote>();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                // For individual Quote objects, we can use default deserialisation:
                Quote quote = context.deserialize(entry.getValue(), Quote.class);
                quotes.add(quote);
            }
            return new QuoteList(quotes);
        }
    }
    JsonParser parser = new JsonParser();
    public String returnJson(String inputString){
        JsonElement jsonTree = parser.parse(inputString);
        if (jsonTree.isJsonObject()){
            JsonObject jsonObject = jsonTree.getAsJsonObject();
        }
        return "Jason Data Done";
//        return outputData;
    }
}
