
import java.io.*;
import java.util.*;

class FieldZone {
    String crop;
    double nitrogen, phosphorus, potassium, pH, rainfall, temperature;

    public FieldZone(String crop, double nitrogen, double phosphorus, double potassium, double pH, double rainfall, double temperature) {
        this.crop = crop;
        this.nitrogen = nitrogen;
        this.phosphorus = phosphorus;
        this.potassium = potassium;
        this.pH = pH;
        this.rainfall = rainfall;
        this.temperature = temperature;
    }

    public void displayInfo() {
        System.out.println("Crop: " + crop + " | N: " + nitrogen + " | P: " + phosphorus + " | K: " + potassium + " | pH: " + pH + " | Rainfall: " + rainfall + " | Temperature: " + temperature);
    }
}

class SoilNutrientTracker {
    private List<FieldZone> data = new ArrayList<>();
    private int numZones = 0;
    private final int numParameters = 6; // N, P, K, pH, Rainfall, Temperature

    public void loadData(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                FieldZone zone = new FieldZone(
                    row[1],
                    Double.parseDouble(row[2]),
                    Double.parseDouble(row[3]),
                    Double.parseDouble(row[4]),
                    Double.parseDouble(row[5]),
                    Double.parseDouble(row[6]),
                    Double.parseDouble(row[7])
                );
                data.add(zone);
                numZones++;
            }
            System.out.println("Data loaded successfully!");
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public void updateValue(int zoneIndex, int parameterIndex, double value) {
        if (zoneIndex >= 0 && zoneIndex < numZones && parameterIndex >= 0 && parameterIndex < numParameters) {
            FieldZone zone = data.get(zoneIndex);
            switch (parameterIndex) {
                case 0 -> zone.nitrogen = value;
                case 1 -> zone.phosphorus = value;
                case 2 -> zone.potassium = value;
                case 3 -> zone.pH = value;
                case 4 -> zone.rainfall = value;
                case 5 -> zone.temperature = value;
                default -> System.out.println("Invalid parameter index.");
            }
            System.out.println("Value updated for zone " + zoneIndex + ", parameter " + parameterIndex);
        } else {
            System.out.println("Invalid zone index or parameter index.");
        }
    }

    public double getValue(int zoneIndex, int parameterIndex) {
        if (zoneIndex >= 0 && zoneIndex < numZones && parameterIndex >= 0 && parameterIndex < numParameters) {
            FieldZone zone = data.get(zoneIndex);
            return switch (parameterIndex) {
                case 0 -> zone.nitrogen;
                case 1 -> zone.phosphorus;
                case 2 -> zone.potassium;
                case 3 -> zone.pH;
                case 4 -> zone.rainfall;
                case 5 -> zone.temperature;
                default -> Double.NaN;
            };
        } else {
            System.out.println("Invalid zone index or parameter index.");
            return Double.NaN;
        }
    }

    public void trendAnalysis() {
        long highNitrogenZones = data.stream().filter(zone -> zone.nitrogen > 100).count();
        long optimalPHZones = data.stream().filter(zone -> zone.pH >= 6.0 && zone.pH <= 7.5).count();
        
        System.out.println("Trend Analysis:");
        System.out.println("Zones with high nitrogen (>100): " + highNitrogenZones);
        System.out.println("Zones with optimal pH (6.0-7.5): " + optimalPHZones);
    }

    public void generateReport() {
        long highNitrogenZones = data.stream().filter(zone -> zone.nitrogen > 100).count();
        long optimalPHZones = data.stream().filter(zone -> zone.pH >= 6.0 && zone.pH <= 7.5).count();

        System.out.println("\nSoil Nutrient Report:");
        System.out.println("Total number of zones: " + numZones);
        System.out.println("Zones with high nitrogen (>100): " + highNitrogenZones);
        System.out.println("Zones with optimal pH (6.0-7.5): " + optimalPHZones);
        System.out.println("Actionable Insights:");
        System.out.println("1. Consider reducing nitrogen levels in high nitrogen zones.");
        System.out.println("2. Zones with optimal pH levels are ideal for a variety of crops.");
    }

    public void displayData() {
        for (FieldZone zone : data) {
            zone.displayInfo();
        }
    }

    public static void main(String[] args) {
        SoilNutrientTracker tracker = new SoilNutrientTracker();
        tracker.loadData("C:\\Users\\Shivananda\\Downloads\\Train Dataset.csv");

        tracker.displayData();
        tracker.trendAnalysis();
        tracker.generateReport();
        
        // Example of updating a value
        tracker.updateValue(0, 0, 120); // Update nitrogen value of the first zone to 120
        System.out.println("Updated Nitrogen Value: " + tracker.getValue(0, 0));
    }
}