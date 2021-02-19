package com.latihan.demo.ui;

import com.latihan.demo.model.Mahasiswa;
import com.latihan.demo.repository.MasterData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Controller
public class HomeAction {

    @Autowired
    private MasterData masterData;

    @GetMapping("/helloui/{id}")
//    public String helloWorld(@PathVariable(required = false, name = "id") int id, @RequestParam(required = false, name = "cek") String cekVal, ModelMap mapParam) {
    public String helloWorld(@PathVariable(required = false, name = "id") int id,
                             @RequestParam(required = false) Map<String, Object> cekVal,
                             ModelMap mapParam) {
        mapParam.put("iden", id);
        mapParam.put("cek", cekVal);
        return "Beranda";
    }

    @GetMapping("/listmhs")
    public String daftarMahasiswa( @RequestParam(required = false, name = "cari") String cari, ModelMap mapParam) {
        mapParam.put("cek", masterData.fetchMahasiswaSql2o(cari));
        return "daftarmhs";
    }

    @GetMapping("/addmhs")
    public String addMhs(Model model) {
        model.addAttribute("mmhs", new Mahasiswa());
        return "addmhs";
    }

    @PostMapping("/savemahasiswa")
    public String saveMahasiswa( Mahasiswa mahasiswa, @RequestParam(required = false, name = "cari") String cari,
                                 Model mapParam) {
        if (mahasiswa.getNimId() > 0){
            masterData.updateMahasiswa(mahasiswa);
        } else {
            masterData.insertMahasiswa(mahasiswa);
        }
        return "redirect:listmhs";
    }

    @GetMapping("/updatemhs/{id}")
    public String updateMhs(@PathVariable("id") String id, Model model) {
        model.addAttribute("mmhs", masterData.fetchMahasiswaByNama(id));
        return "addmhs";
    }


}
