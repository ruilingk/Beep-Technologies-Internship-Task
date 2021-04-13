package Functions;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.DAYS;

public class FunctionFive extends Function {

    HashMap<String, String> orderIDAndSellerID = new HashMap<>();
    HashMap<String, PairOfSumAndReviews> sellerIDAndReviewScore = new HashMap<>();
    HashMap<String, Integer> sellerIDAndAverageReviewScore = new HashMap<>();
    HashMap<String, Integer> sortedSellerIDAndAverageReviewScore = new HashMap<>();

    HashMap<String, String> orderIDAndProductID = new HashMap<>();
    HashMap<String, Integer> orderIDAndPaymentValue = new HashMap<>();
    HashMap<String, Integer> productIDAndPaymentValue = new HashMap<>();
    HashMap<String, String> productIDAndName = new HashMap<>();
    HashMap<String, Integer> productNameAndRevenue = new HashMap<>();
    HashMap<String, String> categoryNameAndEnglishName = new HashMap<>();
    HashMap<String, Integer> englishNameAndRevenue = new HashMap<>();
    HashMap<String, Integer> sortedEnglishNameAndRevenue = new HashMap<>();

    HashMap<String, Integer> sellerIDAndPaymentValue = new HashMap<>();
    HashMap<String, LocalDate> orderIDAndOrderApprovedAt = new HashMap<>();
    HashMap<String, PairOfFirstAndLastDate> sellerIDAndDates = new HashMap<>();
    HashMap<String, Integer> sellerIDAndDays = new HashMap<>();
    HashMap<String, Integer> sortedSellerIDAndPaymentValue = new HashMap<>();

    HashMap<String, String> sellerIDAndSellerState = new HashMap<>();
    HashMap<String, Integer> sellerStateAndPaymentValue = new HashMap<>();
    HashMap<String, Integer> sellerStateAndMonths = new HashMap<>();

    /*
        List the top 10 sellers with the highest review score. (order_reviews:order_id -> order_items:seller_id)
        Calculate the top 10 product categories with the highest sales and show their english name. (order_reviews:order_id -> order_items:product_id -> products:product_category_name -> product_category_name_translation:product_category_name_english)
        Count the daily average revenue by the top 10 sellers with the highest total revenue. (order_payments:payment_value)
        Count the monthly revenue in each seller state.
        If you are a seller, what kind of products you will sell? Where are you going to open your store? And why?
     */

    String USER_DIRECTORY = System.getProperty("user.dir");
    String CUSTOMERS_PATH = "data/customers.csv";
    String ORDER_ITEMS_PATH = "data/order_items.csv";
    String ORDER_PAYMENTS_PATH = "data/order_payments.csv";
    String ORDER_REVIEWS_PATH = "data/order_reviews.csv";
    String ORDERS_PATH = "data/orders.csv";
    String PRODUCT_CATEGORY_NAME_TRANSLATION_PATH = "data/product_category_name_translation.csv";
    String PRODUCTS_PATH = "data/products.csv";
    String SELLERS_PATH = "data/sellers.csv";

    @Override
    public void execute() throws IOException {
        topTenSellersWithHighestReviewScore();
        topTenProductWithHighestSales();
        dailyAverageRevenueByTopTenSellers();
        monthlyRevenueInEachState();
        respond();
    }

    private static class PairOfSumAndReviews {
        private final int sum;
        private final int numberOfReviews;

        public PairOfSumAndReviews(int sum, int numberOfReviews) {
            this.sum = sum;
            this.numberOfReviews = numberOfReviews;
        }

        public int getSum() {
            return this.sum;
        }

        public int getNumberOfReviews() {
            return this.numberOfReviews;
        }
    }

    private static class PairOfFirstAndLastDate {
        private final LocalDate firstDate;
        private final LocalDate lastDate;

        public PairOfFirstAndLastDate(LocalDate firstDate, LocalDate lastDate) {
            this.firstDate = firstDate;
            this.lastDate = lastDate;
        }

        public LocalDate getFirstDate() {
            return this.firstDate;
        }

        public LocalDate getLastDate() {
            return this.lastDate;
        }
    }

    // sort the hashmap based on values, then keys if values are same
    private static HashMap<String, Integer> sortByValue(HashMap<String, Integer> unsortedMap) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortedMap.entrySet());

        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey,
                Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));
    }

    private void topTenSellersWithHighestReviewScore() throws IOException {
        try (
            Reader reader = Files.newBufferedReader(Paths.get(ORDER_ITEMS_PATH));
            CSVReader csvReader = new CSVReader(reader);
        ) {
            String[] nextRecord;
            int count = 0;
            while ((nextRecord = csvReader.readNext()) != null) {
                if (count > 0) {
//                    System.out.println(Arrays.toString(nextRecord));
                    String orderID = nextRecord[0];
                    String sellerID = nextRecord[1];
                    orderIDAndSellerID.put(orderID, sellerID);
                }
                count++;
            }
            reader.close();
            csvReader.close();
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

//        BufferedReader csvReader = new BufferedReader(new FileReader(ORDER_REVIEWS_PATH));
//        String row;
//        while ((row = csvReader.readLine()) != null) {
//            String[] data = row.split(",");
//            System.out.println(data[1]);
//            System.out.println(data[2]);
//            // do something with the data
//        }
//        csvReader.close();

        try (
            Reader reader = Files.newBufferedReader(Paths.get(ORDER_REVIEWS_PATH));
            CSVReader csvReader = new CSVReader(reader);
        ) {
            String[] nextRecord;
            int count = 0;
            while ((nextRecord = csvReader.readNext()) != null) {
                System.out.println(nextRecord.length);
                if (count > 0) {
                    System.out.println(Arrays.toString(nextRecord));
                    String orderID = nextRecord[1];
                    int reviewScore = Integer.parseInt(nextRecord[2]);
                    String sellerID = orderIDAndSellerID.get(orderID);
                    if (!sellerIDAndReviewScore.containsKey(sellerID)) {
                        sellerIDAndReviewScore.put(sellerID, new PairOfSumAndReviews(reviewScore, 1));
                    } else {
                        PairOfSumAndReviews pairOfSumAndReviews = sellerIDAndReviewScore.get(sellerID);
                        int currentSum = pairOfSumAndReviews.getSum();
                        int currentNumberOfReviews = pairOfSumAndReviews.getNumberOfReviews();
                        currentSum += reviewScore;
                        currentNumberOfReviews++;
                        sellerIDAndReviewScore.put(sellerID, new PairOfSumAndReviews(currentSum, currentNumberOfReviews));
                    }
                }
                count++;

            }
            reader.close();
            csvReader.close();
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        Iterator<Map.Entry<String, PairOfSumAndReviews>> it = sellerIDAndReviewScore.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, PairOfSumAndReviews> pair = it.next();
            String sellerID = pair.getKey();
            int totalSum = pair.getValue().getSum();
            int totalNumberOfReviews = pair.getValue().getNumberOfReviews();
            int average = totalSum / totalNumberOfReviews;
            sellerIDAndAverageReviewScore.put(sellerID, average);
        }

        sortedSellerIDAndAverageReviewScore = sortByValue(sellerIDAndAverageReviewScore);
        Iterator<Map.Entry<String, Integer>> iterator = sortedSellerIDAndAverageReviewScore.entrySet().iterator();

        System.out.println("Top 10 sellers with high review scores:");
        int count = 0;
        while (iterator.hasNext() && count < 10) {
            Map.Entry<String, Integer> pair = iterator.next();
            String sellerID = pair.getKey();
            int averageReviewScore = pair.getValue();
            count++;
            System.out.println(count + ". Seller ID: " + sellerID + " Average Review Score: " + averageReviewScore);
        }
    }

    private void topTenProductWithHighestSales() {
        try (
            Reader reader = Files.newBufferedReader(Paths.get(ORDER_ITEMS_PATH));
            CSVReader csvReader = new CSVReader(reader);
        ) {
            String[] nextRecord;
            int count = 0;
            while ((nextRecord = csvReader.readNext()) != null) {
                if (count > 0) {
                    String orderID = nextRecord[0];
                    String productID = nextRecord[2];
                    orderIDAndProductID.put(orderID, productID);
                }
                count++;
            }
            reader.close();
            csvReader.close();
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        try (
            Reader reader = Files.newBufferedReader(Paths.get(ORDER_PAYMENTS_PATH));
            CSVReader csvReader = new CSVReader(reader);
        ) {
            String[] nextRecord;
            int count = 0;
            while ((nextRecord = csvReader.readNext()) != null) {
                if (count > 0) {
                    String orderID = nextRecord[0];
                    int paymentValue = Integer.parseInt(nextRecord[4]);
                    orderIDAndPaymentValue.put(orderID, paymentValue);
                }
                count++;
            }
            reader.close();
            csvReader.close();
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        Iterator<Map.Entry<String, String>> it1 = orderIDAndProductID.entrySet().iterator();

        while (it1.hasNext()) {
            Map.Entry<String, String> pair1 = it1.next();
            String orderID = pair1.getKey();
            String productID = pair1.getValue();
            int paymentValue = orderIDAndPaymentValue.get(orderID);
            if (!productIDAndPaymentValue.containsKey(productID)) {
                productIDAndPaymentValue.put(productID, paymentValue);
            } else {
                int currentRevenue = productIDAndPaymentValue.get(productID);
                currentRevenue += paymentValue;
                productIDAndPaymentValue.put(productID, currentRevenue);
            }
        }

        try (
            Reader reader = Files.newBufferedReader(Paths.get(PRODUCTS_PATH));
            CSVReader csvReader = new CSVReader(reader);
        ) {
            String[] nextRecord;
            int count = 0;
            while ((nextRecord = csvReader.readNext()) != null) {
                if (count > 0) {
                    String productID = nextRecord[0];
                    String productName = nextRecord[1];
                    productIDAndName.put(productID, productName);
                }
                count++;
            }
            reader.close();
            csvReader.close();
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        Iterator<Map.Entry<String, Integer>> it2 = productIDAndPaymentValue.entrySet().iterator();

        while (it2.hasNext()) {
            Map.Entry<String, Integer> pair1 = it2.next();
            String productID = pair1.getKey();
            int paymentValue = pair1.getValue();
            String productName = productIDAndName.get(productID);
            if (!productNameAndRevenue.containsKey(productName)) {
                productNameAndRevenue.put(productName, paymentValue);
            } else {
                int currentRevenue = productNameAndRevenue.get(productName);
                currentRevenue += paymentValue;
                productNameAndRevenue.put(productName, currentRevenue);
            }
        }

        try (
            Reader reader = Files.newBufferedReader(Paths.get(PRODUCT_CATEGORY_NAME_TRANSLATION_PATH));
            CSVReader csvReader = new CSVReader(reader);
        ) {
            String[] nextRecord;
            int count = 0;
            while ((nextRecord = csvReader.readNext()) != null) {
                if (count > 0) {
                    String categoryName = nextRecord[0];
                    String englishName = nextRecord[1];
                    categoryNameAndEnglishName.put(categoryName, englishName);
                }
                count++;
            }
            reader.close();
            csvReader.close();
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        Iterator<Map.Entry<String, Integer>> it3 = productNameAndRevenue.entrySet().iterator();

        while (it3.hasNext()) {
            Map.Entry<String, Integer> pair1 = it3.next();
            String productName = pair1.getKey();
            int revenue = pair1.getValue();
            String englishName = categoryNameAndEnglishName.get(productName);
            if (!englishNameAndRevenue.containsKey(englishName)) {
                englishNameAndRevenue.put(englishName, revenue);
            } else {
                int currentRevenue = englishNameAndRevenue.get(englishName);
                currentRevenue += revenue;
                englishNameAndRevenue.put(englishName, currentRevenue);
            }
        }
        sortedEnglishNameAndRevenue = sortByValue(englishNameAndRevenue);
        Iterator<Map.Entry<String, Integer>> iterator = sortedEnglishNameAndRevenue.entrySet().iterator();

        System.out.println("Top 10 product categories with high revenues:");
        int count = 0;
        while (iterator.hasNext() && count < 10) {
            Map.Entry<String, Integer> pair = iterator.next();
            String englishName = pair.getKey();
            int revenue = pair.getValue();
            count++;
            System.out.println(count + ". English name of product category: " + englishName +
                    " Revenue earned: " + revenue);
        }
    }

    private void dailyAverageRevenueByTopTenSellers() {
        Iterator<Map.Entry<String, String>> it1 = orderIDAndSellerID.entrySet().iterator();

        while (it1.hasNext()) {
            Map.Entry<String, String> pair1 = it1.next();
            String orderID = pair1.getKey();
            String sellerID = pair1.getValue();
            int paymentValue = orderIDAndPaymentValue.get(orderID);
            if (!sellerIDAndPaymentValue.containsKey(sellerID)) {
                sellerIDAndPaymentValue.put(sellerID, paymentValue);
            } else {
                int currentRevenue = sellerIDAndPaymentValue.get(sellerID);
                currentRevenue += paymentValue;
                sellerIDAndPaymentValue.put(sellerID, currentRevenue);
            }
        }

        try (
            Reader reader = Files.newBufferedReader(Paths.get(ORDERS_PATH));
            CSVReader csvReader = new CSVReader(reader);
        ) {
            String[] nextRecord;
            int count = 0;
            while ((nextRecord = csvReader.readNext()) != null) {
                if (count > 0) {
                    String orderID = nextRecord[0];
                    String[] splitDate = nextRecord[4].split(" ");
                    LocalDate orderApprovedAt = LocalDate.parse(splitDate[0]);
                    orderIDAndOrderApprovedAt.put(orderID, orderApprovedAt);
                }
                count++;
            }
            reader.close();
            csvReader.close();
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        while (it1.hasNext()) {
            Map.Entry<String, String> pair1 = it1.next();
            String orderID = pair1.getKey();
            String sellerID = pair1.getValue();
            LocalDate date = orderIDAndOrderApprovedAt.get(orderID);
            if (!sellerIDAndDates.containsKey(sellerID)) {
                sellerIDAndDates.put(sellerID, new PairOfFirstAndLastDate(date, null));
            } else {
                PairOfFirstAndLastDate pairOfFirstAndLastDate = sellerIDAndDates.get(sellerID);
                LocalDate firstDate = pairOfFirstAndLastDate.getFirstDate();
                LocalDate lastDate = pairOfFirstAndLastDate.getLastDate();
                if (lastDate == null) {
                    if (firstDate.isBefore(date)) {
                        pairOfFirstAndLastDate = new PairOfFirstAndLastDate(firstDate, date);
                    } else {
                        pairOfFirstAndLastDate = new PairOfFirstAndLastDate(date, firstDate);
                    }
                } else {
                    if (firstDate.isAfter(date)) {
                        pairOfFirstAndLastDate = new PairOfFirstAndLastDate(date, lastDate);
                    } else if (lastDate.isBefore(date)) {
                        pairOfFirstAndLastDate = new PairOfFirstAndLastDate(firstDate, date);
                    }
                }
                sellerIDAndDates.put(sellerID, pairOfFirstAndLastDate);
            }
        }

        Iterator<Map.Entry<String, PairOfFirstAndLastDate>> it2 = sellerIDAndDates.entrySet().iterator();

        while (it2.hasNext()) {
            Map.Entry<String, PairOfFirstAndLastDate> pair1 = it2.next();
            String sellerID = pair1.getKey();
            LocalDate first = pair1.getValue().getFirstDate();
            LocalDate last = pair1.getValue().getLastDate();
            int numberOfDays = (int) first.until(last, DAYS.toChronoUnit());
            sellerIDAndDays.put(sellerID, numberOfDays);
        }

        sortedSellerIDAndPaymentValue = sortByValue(sellerIDAndPaymentValue);
        Iterator<Map.Entry<String, Integer>> it3 = sortedSellerIDAndPaymentValue.entrySet().iterator();

        System.out.println("Daily Average Revenue By Top Ten Sellers: ");
        int count = 0;
        while (it3.hasNext() && count < 10) {
            Map.Entry<String, Integer> pair1 = it3.next();
            String sellerID = pair1.getKey();
            int days = sellerIDAndDays.get(sellerID);
            int payment = pair1.getValue();
            int dailyAverageRevenue = payment / days;
            System.out.println("Seller ID: " + sellerID + " , Daily average revenue: " + dailyAverageRevenue);
            count++;
        }
    }

    private void monthlyRevenueInEachState() {
        try (
            Reader reader = Files.newBufferedReader(Paths.get(SELLERS_PATH));
            CSVReader csvReader = new CSVReader(reader);
        ) {
            String[] nextRecord;
            int count = 0;
            while ((nextRecord = csvReader.readNext()) != null) {
                if (count > 0) {
                    String sellerID = nextRecord[0];
                    String sellerState = nextRecord[3];
                    sellerIDAndSellerState.put(sellerID, sellerState);
                }
                count++;
            }
            reader.close();
            csvReader.close();
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        Iterator<Map.Entry<String, Integer>> it1 = sellerIDAndPaymentValue.entrySet().iterator();

        while (it1.hasNext()) {
            Map.Entry<String, Integer> pair1 = it1.next();
            String sellerID = pair1.getKey();
            String sellerState = sellerIDAndSellerState.get(sellerID);
            int paymentValue = pair1.getValue();
            if (!sellerStateAndPaymentValue.containsKey(sellerState)) {
                sellerStateAndPaymentValue.put(sellerState, paymentValue);
            } else {
                int currentRevenue = sellerStateAndPaymentValue.get(sellerState);
                currentRevenue += paymentValue;
                sellerStateAndPaymentValue.put(sellerState, currentRevenue);
            }
        }

        Iterator<Map.Entry<String, String>> it2 = sellerIDAndSellerState.entrySet().iterator();

        while (it2.hasNext()) {
            Map.Entry<String, String> pair1 = it2.next();
            String sellerID = pair1.getKey();
            int months = sellerIDAndDays.get(sellerID) / 365;
            String sellerState = pair1.getValue();
            if (!sellerStateAndMonths.containsKey(sellerState)) {
                sellerStateAndMonths.put(sellerState, months);
            } else {
                int currentMonths = sellerStateAndMonths.get(sellerState);
                currentMonths += months;
                sellerStateAndMonths.put(sellerState, currentMonths);
            }
        }

        Iterator<Map.Entry<String, Integer>> it3 = sellerStateAndPaymentValue.entrySet().iterator();

        System.out.println("Monthly Revenue In Each State: ");
        while (it3.hasNext()) {
            Map.Entry<String, Integer> pair1 = it3.next();
            String sellerState = pair1.getKey();
            int payment = pair1.getValue();
            int months = sellerStateAndMonths.get(sellerState);
            int monthlyRevenue = payment / months;
            System.out.println("Seller state: " + sellerState + " Monthly revenue: " + monthlyRevenue);
        }
    }

    private void respond() {
        System.out.println("If I am a seller, I will sell ... I am going to open a store in ... because ...");
    }
}
