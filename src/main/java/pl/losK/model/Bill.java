package pl.losK.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.time.LocalDate;
import java.util.*;

@XmlRootElement(name = "Bill")
@XmlType(propOrder = {"id", "listOfItems", "date", "price", "tax", "payment"})
public class Bill {

    private UUID id;
    private List<BillItem> listOfItems;
    private LocalDate date;
    private Double price;
    private Double tax;
    private Payment payment;

    public Bill() {
        id = UUID.randomUUID();
        date = getDate();
        listOfItems = new ArrayList<>();

        price = 0.0;
        tax = 0.0;

        payment = Payment.CASH;
    }

    public void addItem(BillItem item, Integer amountBought) {

        Integer addedAmount = item.getAmount() * amountBought;

        if (listOfItems.contains(item)) {
            Integer oldAmount = listOfItems.get(listOfItems.indexOf(item)).getAmount();
            Integer newAmount = oldAmount + addedAmount;
            listOfItems.get(listOfItems.indexOf(item)).setAmount(newAmount);
        } else {
            item.setAmount(addedAmount);
            listOfItems.add(item);
        }

        price += item.getPrice() * addedAmount;
        tax += item.getTax() * addedAmount * item.getTax();
    }

    public void removeItem(BillItem item, Integer amountToRemove) {
        Integer removedAmount = item.getAmount() * amountToRemove;

        if (listOfItems.contains(item)) {
            Integer oldAmount = listOfItems.get(listOfItems.indexOf(item)).getAmount();
            Integer newAmount = oldAmount - removedAmount;
            listOfItems.get(listOfItems.indexOf(item)).setAmount(newAmount);

            if (listOfItems.get(listOfItems.indexOf(item)).getAmount() <= 0) {
                price -= item.getPrice() * oldAmount;
                tax -= item.getPrice() * oldAmount * item.getTax();

                listOfItems.remove(item);
            } else {
                price -= item.getPrice() * removedAmount;
                tax -= item.getPrice() * removedAmount * item.getTax();
            }
        }
    }

    public void updateItem(BillItem item, Integer amountBought) {

        if (amountBought < 0) {
            amountBought = 0;
        }

        Integer addedAmount = amountBought;

        if (listOfItems.contains(item)) {

            Integer oldAmount = listOfItems.get(listOfItems.indexOf(item)).getAmount();

            Integer newAmount = addedAmount;
            listOfItems.get(listOfItems.indexOf(item)).setAmount(newAmount);


        } else {
            item.setAmount(addedAmount);
            listOfItems.add(item);
        }

        setPrice();
        setTax();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<BillItem> getListOfItems() {
        return listOfItems;
    }

    public void setListOfItems(List<BillItem> listOfItems) {
        this.listOfItems = listOfItems;
    }

    public LocalDate getDate() {
        return date;
    }

    public Double getPrice() {
        return price;
    }

    private void setPrice() {
        price = 0.0;
        for (BillItem e : this.getListOfItems()) {
            price += e.getAmount() * e.getPrice();
        }
    }

    public Double getTax() {
        return tax;
    }

    public void setTax() {
        tax = 0.0;
        for (BillItem e : this.getListOfItems()) {
            tax += e.getAmount() * e.getPrice() * e.getTax();
        }
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", listOfItems=" + listOfItems +
                ", date=" + date +
                ", price=" + price +
                ", tax=" + tax +
                ", payment=" + payment +
                '}';
    }
}
