# PAE-Sync

A multi-database *academic program manager (PAE)* built with Spring Boot and Vue.js.

---

## Overview

**PAE-Sync** (Programme Annuel des Études – “Annual Study Program”) is a web application designed to help manage university course programs and their prerequisites.  
It demonstrates how to synchronize and manage data across **multiple databases** — MongoDB, Neo4j, and Elasticsearch — within a unified Spring Boot backend.

This project is an evolution of a previous PAE web app that used Spring Boot, Thymeleaf, and H2.  
While the original focused on CRUD and data validation, **PAE-Sync** explores **multi-model persistence** — using the best database type for each kind of data.

---

## Architecture

| Layer | Technology | Purpose |
|-------|-------------|----------|
| **Frontend** | Vue.js | Dynamic and reactive user interface |
| **Backend** | Spring Boot | REST API and synchronization logic |
| **Database 1** | MongoDB | Stores course metadata |
| **Database 2** | Neo4j | Stores prerequisite relationships between courses |
| **Database 3** | Elasticsearch | Provides advanced search and fuzzy matching |

---

## Demo

A few highlights of **PAE-Sync** in action — showing how courses are created, synchronized, and queried across multiple databases.

### Adding a Course
<p align="center">
  <img src="./assets/demo/adding-course.gif" width="700" alt="Adding a new course in PAE-Sync">
</p>

---

### Data Synchronization (MongoDB + Neo4j)
<p align="center">
  <img src="./assets/demo/course-added-to-mongodb-and-neo4j.gif" width="700" alt="Course synchronized between MongoDB and Neo4j">
</p>

---

### Adding a Prerequisite
<p align="center">
  <img src="./assets/demo/adding-a-prerequisite.gif" width="700" alt="Adding a prerequisite link between courses">
</p>

---

### Prerequisite Proof in Neo4j
<p align="center">
  <img src="./assets/demo/prerequisite-added-proof.gif" width="700" alt="Verifying prerequisite relationship in Neo4j">
</p>

---

### Fuzzy Search (Elasticsearch)
<p align="center">
  <img src="./assets/demo/elasticsearch-demo.gif" width="700" alt="Fuzzy search demonstration powered by Elasticsearch">
</p>

---
