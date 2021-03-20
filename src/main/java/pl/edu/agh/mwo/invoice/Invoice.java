package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    
    private final int number = Math.abs(new Random().nextInt());
    private Map<Product, Integer> products = new HashMap<Product, Integer>();

    public void addProduct(Product product) {
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException();
        }
        for (Map.Entry<Product, Integer> p : products.entrySet()) {
            if (p.getKey().getName().equals(product.getName())) {
                products.replace(p.getKey(), p.getValue(), quantity + p.getValue());
                return;
            }
        }
        products.put(product, quantity);
    }

    public BigDecimal getNetTotal() {
        BigDecimal totalNet = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalNet = totalNet.add(product.getPrice().multiply(quantity));
        }
        return totalNet;
    }

    public BigDecimal getTaxTotal() {
        return getGrossTotal().subtract(getNetTotal());
    }

    public BigDecimal getGrossTotal() {
        BigDecimal totalGross = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalGross = totalGross.add(product.getPriceWithTax().multiply(quantity));
        }
        return totalGross;
    }

    public int getNumber() {
        return number;
    }
    
    public void printInvoice() {
        System.out.println("Faktura: " + getNumber());
        System.out.println("Nazwa    Ilosc    Cena");
        int n = 0;
        for (Map.Entry<Product, Integer> p : products.entrySet()) {
            System.out.println(p.getKey().getName() + "   " + p.getValue()
                + "   " + p.getKey().getPriceWithTax());
            n = n + p.getValue();
        }
        System.out.println("Liczba pozycji: " + n);
    }
}
