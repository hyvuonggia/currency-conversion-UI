package com.company;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Controller {
    @FXML
    public TextField textFieldFrom;
    @FXML
    public TextField textFieldTo;
    @FXML
    public ComboBox<String> comboBoxFrom;
    @FXML
    public ComboBox<String> comboBoxTo;
    @FXML
    public Button buttonConvert;
    @FXML
    public Label labelDate;

    private String responseBody;

    public void initialize() {
        comboBoxFrom.setValue("USD - United States dollar");
        comboBoxTo.setValue("VND - Vietnamese đồng");
        textFieldTo.setEditable(false);

    }

    public void convert() {
        try {
            responseBody();
        } catch (NumberFormatException e) {

        }
//        System.out.println(responseBody);

        textFieldTo.setText(rateForAmount());

        labelDate.setText("Updated: " + updatedDate(responseBody));
    }

    public void responseBody() {
        String currencyFrom = comboBoxFrom.getValue().substring(0, 3);
        String currencyTo = comboBoxTo.getValue().substring(0, 3);

        double amount = Double.parseDouble(textFieldFrom.getText());

        String uri = "https://currency-converter5.p.rapidapi.com/currency/convert?format=json" +
                "&from=" + currencyFrom +
                "&to=" + currencyTo +
                "&amount=" + amount;

        String API_KEY = "849b590626msha2d99165166d1f6p162ec1jsnc3306391c1f1";
        HttpRequest request = HttpRequest.newBuilder()

                .uri(URI.create(uri))
                .header("x-rapidapi-key", API_KEY)
                .header("x-rapidapi-host", "currency-converter5.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            responseBody = response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            responseBody = null;
        }
    }

    public String rateForAmount() {
        JSONObject json = new JSONObject(responseBody);
        JSONObject rates = json.getJSONObject("rates");
        JSONObject currency = rates.getJSONObject(comboBoxTo.getValue().substring(0, 3));

        String rateForAmount = currency.getString("rate_for_amount");

        return rateForAmount;
    }

    public String updatedDate(String response) {
        JSONObject json = new JSONObject(response);

        String date = json.getString("updated_date");

        return date;
    }

}
