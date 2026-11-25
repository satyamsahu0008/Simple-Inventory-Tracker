# 📦 Simple Inventory Tracker

A Java-based inventory management application built using **Data Structures and Algorithms (DSA)**. It helps manage products, track stock quantities, identify low-stock items, and process restocking requests.

## 🚀 Features

- ➕ Add new products
- 📋 View all inventory products
- 🔍 Search products using Product ID
- ✏️ Update product details
- 🗑️ Delete products
- 📥 Stock In – Increase product quantity
- 📤 Stock Out – Decrease product quantity
- ⚠️ Low-stock alerts
- 🔃 Sort products by price, name, and quantity
- 📦 Manage restocking requests using FIFO Queue
- ✅ Prevent duplicate product IDs and restock requests
- 🛡️ Input validation and stock quantity checks

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java | Core programming language |
| HashMap | Product storage and searching |
| ArrayList | Product listing and sorting |
| Queue | FIFO restock management |
| HashSet | Prevent duplicate restock requests |
| Comparator | Sorting products |

## 📂 Project Structure

```text
Simple-Inventory-Tracker/
│
├── Main.java
│   ├── Product
│   ├── InventoryManager
│   └── Main
│
└── README.md
```

## ⚙️ Installation and Setup

### 1. Clone the repository

```bash
git clone https://github.com/satyamsahu0008/Simple-Inventory-Tracker.git
```

### 2. Navigate to the project directory

```bash
cd Simple-Inventory-Tracker
```

### 3. Check Java installation

```bash
java -version
javac -version
```

### 4. Compile the Java program

```bash
javac Main.java
```

### 5. Run the application

```bash
java Main
```

## 💻 Application Menu

```text
===== SIMPLE INVENTORY TRACKER =====

1. Add Product
2. View All Products
3. Search Product by ID
4. Update Product Details
5. Delete Product
6. Stock In (Add Quantity)
7. Stock Out (Sell/Remove Quantity)
8. Show Low-Stock Products
9. Sort Products by Price
10. Sort Products by Name
11. Sort Products by Quantity
12. View Restock Queue
13. Process Next Restock Request
0. Exit

Enter your choice:
```

## 🧠 Data Structures and Algorithms

### 1. HashMap

Used to store products using their unique Product IDs.

```java
Map<Integer, Product> products = new HashMap<>();
```

Average search complexity: **O(1)**

### 2. ArrayList

Used to collect and display product records and perform sorting operations.

```java
List<Product> list = new ArrayList<>(products.values());
```

### 3. Queue

Used to manage pending restock requests using the FIFO principle.

```java
Queue<Integer> restockQueue = new LinkedList<>();
```

### 4. HashSet

Used to prevent duplicate product IDs in the restock queue.

```java
Set<Integer> queuedProductIds = new HashSet<>();
```

### 5. Sorting

Products can be sorted by price, name, and available quantity using Java Comparator.

## ⏱️ Time Complexity Analysis

| Operation | Average Time Complexity |
|---|---|
| Add Product | O(1) |
| Search Product | O(1) |
| Delete Product | O(1) average |
| Update Product | O(1) average |
| Stock In / Stock Out | O(1) average |
| Display All Products | O(n log n) |
| Low-Stock Detection | O(n) |
| Sorting | O(n log n) |
| Queue Enqueue | O(1) |
| Queue Dequeue | O(1) |

*Note: n represents the total number of products.*

## 📸 Application Screenshots

Add screenshots of your actual running application here.

Suggested screenshots:
- Main Menu
- Add Product
- View All Products
- Search Product
- Low-Stock Alert
- Sorting Products
- Restock Queue

## 🎯 Project Objectives

- Apply Java programming concepts to a real-world problem.
- Understand and implement fundamental data structures.
- Improve product searching and inventory management.
- Demonstrate FIFO queue operations.
- Analyze the time complexity of different operations.

## 🔮 Future Enhancements

- MySQL database integration for permanent data storage.
- User authentication and role-based access.
- GUI using Java Swing or JavaFX.
- Supplier and purchase management.
- Inventory reports and analytics.
- Barcode scanning functionality.

## ⚠️ Current Limitations

- Console-based application.
- Data is stored in memory.
- Inventory records are lost when the application exits.
- No multi-user support.

## 📚 References

- [Oracle Java Documentation](https://docs.oracle.com/en/java/)
- [Java Collections Framework](https://docs.oracle.com/javase/8/docs/technotes/guides/collections/)
- [GeeksforGeeks](https://www.geeksforgeeks.org/)

## 👨‍💻 Author

**Developed as a Data Structures and Algorithms (DSA) academic project.**

---

⭐ If you find this project useful, consider giving it a star!
