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
        public double open = 0;
        public long openTime = 0;              // Epoch time
        public double close = 0;
        public long closeTime = 0;
        public double high = 0;
        public double low = 0;
        public double latestPrice = 0;
        public String latestSource = null;
        public String latestTime = null;
        public long latestUpdate= 0;           // Epoch time
        public long latestVolume = 0;
        public double iexRealtimePrice = 0;
        public long iexRealtimeSize = 0;
        public long  iexLastUpdated = 0;       // Epoch time
        public double delayedPrice = 0;
        public long  delayedPriceTime = 0;              // Epoch time
        public double extendedPrice = 0;
        public double extendedChange = 0;
        public double extendedChangePercent = 0;
        public long extendedPriceTime = 0;              // Epoch time
        public double previousClose = 0;
        public double change = 0;
        public double changePercent = 0;
        public double iexMarketPercent = 0;
        public long  iexVolume = 0;
        public long  avgTotalVolume = 0;
        public double iexBidPrice = 0;
        public long  iexBidSize = 0;
        public double iexAskPrice = 0;
        public long  iexAskSize = 0;
        public long  marketCap = 0;
        public double peRatio = 0;
        public double week52High = 0;
        public double week52Low = 0;
        public double ytdChange = 0;
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
        public double high = 0; //": 143.5275,
        public double low = 0; //": 142.4619,
        public double close = 0;//": 143.1092,
        public long volume = 0; //": 19985714,
        public double unadjustedClose = 0; //": 143.7,
        public long unadjustedVolume = 0; //": 19985714,
        public double change = 0; //": 0.039835,
        public double changePercent = 0; //": 0.028,
        public double vwap = 0; //": 143.0507,
        public String label = null; //": "Apr 03, 17",
        public double changeOverTime = 0; //": -0.0039
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
    public static class WatchListData{
        private String symbol;
        private String name;
        private double price;
        private double change;

        public void setPrice(double price){
            this.price = price;
        }
        public void setChange(double change){
            this.change = change;
        }
        public void setSymbol(String symbol){ this.symbol = symbol; }
        public void setName(String name){
            this.name = name;
        }

        public double getPrice(){
            return price;
        }
        public double getChange(){
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
                // For individual Quote objects, we can use default deserialization:
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
