package com.example.perpusapi.model;

import java.time.LocalDate;

public class Book {
    private int buku_id,rakbuku_id_fk, jumlah, jml_tersedia;
    private String nama_buku, tipe_buku, jenis_buku, author;
    private LocalDate tgl_terbit;

    private int status_booking;


    public Book(int buku_id, int rakbuku_id_fk, int jumlah, int jml_tersedia, String nama_buku, String tipe_buku, String jenis_buku, String author, LocalDate tgl_terbit, int status_booking) {
        this.buku_id = buku_id;
        this.rakbuku_id_fk = rakbuku_id_fk;
        this.jumlah = jumlah;
        this.jml_tersedia = jml_tersedia;
        this.nama_buku = nama_buku;
        this.tipe_buku = tipe_buku;
        this.jenis_buku = jenis_buku;
        this.author = author;
        this.tgl_terbit = tgl_terbit;
        this.status_booking = status_booking;
    }

    public int getBuku_id() {
        return buku_id;
    }

    public void setBuku_id(int buku_id) {
        this.buku_id = buku_id;
    }

    public int getRakbuku_id_fk() {
        return rakbuku_id_fk;
    }

    public void setRakbuku_id_fk(int rakbuku_id_fk) {
        this.rakbuku_id_fk = rakbuku_id_fk;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getJml_tersedia() {
        return jml_tersedia;
    }

    public void setJml_tersedia(int jml_tersedia) {
        this.jml_tersedia = jml_tersedia;
    }

    public String getNama_buku() {
        return nama_buku;
    }

    public void setNama_buku(String nama_buku) {
        this.nama_buku = nama_buku;
    }

    public String getTipe_buku() {
        return tipe_buku;
    }

    public void setTipe_buku(String tipe_buku) {
        this.tipe_buku = tipe_buku;
    }

    public String getJenis_buku() {
        return jenis_buku;
    }

    public void setJenis_buku(String jenis_buku) {
        this.jenis_buku = jenis_buku;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getTgl_terbit() {
        return tgl_terbit;
    }

    public void setTgl_terbit(LocalDate tgl_terbit) {
        this.tgl_terbit = tgl_terbit;
    }

    public int getStatus_booking() {
        return status_booking;
    }

    public void setStatus_booking(int status_booking) {
        this.status_booking = status_booking;
    }
}
