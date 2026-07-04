import java.io.*;
import java.util.*;

class ProductStats{
  double totalAvailability=0;
  int totalSold=0;
  double totalRevenue=0;
  int count=0;
  void add(double availability,int sold,double revenue ){ // Corrected parameter name 'Revenue' to 'revenue'
  totalAvailability+=availability;
  totalSold+=sold;
  totalRevenue+=revenue;
  count++;
   }
}

public class DashboardAnalyzer{
  public static void main(String[] args) {
    Map<String,ProductStats> map=new HashMap<>();
    try{
      BufferedReader br=new BufferedReader(new FileReader("/content/supply_chain_data-selected-columns.csv")); // Use absolute path
      String line=br.readLine(); // Read header
      while ((line=br.readLine())!=null){
        String[] data=line.split(",");
        String productType=data[0];
        double availability=Double.parseDouble(data[3]);
        int sold=Integer.parseInt(data[4]);
        double revenue=Double.parseDouble(data[5]);
        map.putIfAbsent(productType,new ProductStats());
        map.get(productType).add(availability,sold,revenue);
      }
      br.close(); // Close BufferedReader
    }catch(IOException e){
      e.printStackTrace();
    }

    try(PrintWriter out=new PrintWriter("dashboard.html")){
      out.println("<html><head><title>Supply Chain KPI Dashboard</title>");
      out.println("<style>body{font-family:Arial;background:#f4f4f4;padding:20px}table{border-collapse:collapse;width:80%;margin:auto;background:white;box-shadow:0 0 10px#cc}th,td{padding:12px;text-align:center;border:1px solid #ddd}th{background:#4CAF50;color:white}h1{text-align:center;color:#333}</style>");
      out.println("</head><body>");
      out.println("<h1>Supply Chain KPI Dashboard</h1>");
      out.println("<table><tr><th>Product Type</th><th>Availability</th><th>Total Sold</th><th>Avg Revenue</th></tr>");


      for(Map.Entry<String,ProductStats> entry:map.entrySet()){ // Added curly braces for for loop
        ProductStats s=entry.getValue();
        double avgAvail=s.totalAvailability/s.count;
        double avgRev=s.totalRevenue/s.count;
        out.printf("<tr><td>%s</td><td>%.2f</td><td>%d</td><td>%.2f</td></tr>\n",entry.getKey(),avgAvail,s.totalSold,avgRev); // Changed to out.printf and corrected formatting
      }
      out.println("</table></body></html>");
      System.out.println("Dashboard generated successfully!");
    }
    catch(IOException e){
      e.printStackTrace();
    }
  }
}
