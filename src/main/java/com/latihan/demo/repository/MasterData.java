package com.latihan.demo.repository;

import com.latihan.demo.model.Mahasiswa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class MasterData {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private Sql2o sql2oo;

    public List<Mahasiswa> fetchMahasiswaJdbc() {
        return jdbcTemplate.query("SELECT nama as namaID, nim as nimID FROM mahasiswa",
                (resultSet, rowNumber) -> {
                    Mahasiswa mahasiswa = new Mahasiswa();
                    mahasiswa.setNamaId(resultSet.getString("namaID"));
                    mahasiswa.setNimId(resultSet.getInt("nimID"));
                    return mahasiswa;
                });
    }

    public List<Mahasiswa> fetchMahasiswaSql2o(String cari) {
        try (org.sql2o.Connection con = sql2oo.open()) {
            String param;
            if (ObjectUtils.isEmpty(cari)) {
                param = "";
            } else {
                param = cari;
            }
            final String query =
                    "SELECT nama as namaID, nim as nimID FROM mahasiswa WHERE nama LIKE CONCAT ('%',:cari,'%')";

            return con.createQuery(query)
                    .addParameter("cari", param)
                    .executeAndFetch(Mahasiswa.class);
        }
    }

    public Mahasiswa fetchMahasiswaByNama(String cari) {
        try (org.sql2o.Connection con = sql2oo.open()) {
            String nama;
            if (ObjectUtils.isEmpty(cari)) {
                nama = "";
            } else {
                nama = cari;
            }
            final String query =
                    "SELECT nama as namaID, nim as nimID FROM mahasiswa "
                            + " WHERE nama = :namaSelected";

            return con.createQuery(query)
                    .addParameter("namaSelected", nama)
                    .executeAndFetchFirst(Mahasiswa.class);
        }
    }

    public void insertMahasiswaNamedJdbc(Mahasiswa mahasiswa) {

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("nama", mahasiswa.getNamaId());
        parameter.put("nim", mahasiswa.getNimId());

        final String query = "INSERT INTO mahasiswa " +
                "(nama, nim) VALUES (:nama, :nim); ";
        namedParameterJdbcTemplate.update(query, parameter);

        System.out.println("Input berhasil!");
    }

    public void insertMahasiswa(Mahasiswa mahasiswa) {
        try (org.sql2o.Connection con = sql2oo.open()) {
            final String query = "INSERT INTO mahasiswa "
                    + "(nama, nim) VALUES (:namaIn, :nimIn)";

            con.createQuery(query)
                    .addParameter("namaIn", mahasiswa.getNamaId())
                    .addParameter("nimIn", mahasiswa.getNimId())
                    .executeUpdate();
            System.out.println("Input berhasil!");
        }
    }

    public void updateMahasiswa(Mahasiswa mahasiswa) {
        final String query = "UPDATE mahasiswa SET" +
                " nama = ? WHERE nim = ? ";
        jdbcTemplate.update(query,
                mahasiswa.getNamaId(),
                mahasiswa.getNimId());

        System.out.println("Update berhasil!");
    }

}
