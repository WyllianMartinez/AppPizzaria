package com.example.exemploradiocheckbutton;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.Transliterator;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lvSabor;
    private Spinner spSabor;
    private RadioGroup rbGroup;
    private RadioButton rbPequena;
    private RadioButton rbMedia;
    private RadioButton rbGrande;

    private Button btnLimpar;
    private Button btnConcluir;
    private Button btnRemover;
    private CheckBox cbBorda;
    private CheckBox cbRefri;
    private TextView tvPedido;

    private TextView tvTotal;
    private String borda = " ";
    private String refri = " ";

    private String tamanho = " ";

    private double valorTamanho = 0;

    private double valorBorda = 0;

    private double valorRefri = 0;


    private List<String> saboresSelecionados = new ArrayList<>();
    private ArrayAdapter<String> adapterListView;
    private int saborRemover = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rbGroup = findViewById(R.id.rbGroup);
        rbPequena = findViewById(R.id.rbPequena);
        rbMedia = findViewById(R.id.rbMedia);
        rbGrande = findViewById(R.id.rbGrande);

        spSabor = findViewById(R.id.spSabor);
        lvSabor = findViewById(R.id.lvSabor);
        btnRemover = findViewById(R.id.btnRemover);

        btnConcluir = findViewById(R.id.btnConcluir);

        tvPedido = findViewById(R.id.tvPedido);
        tvTotal = findViewById(R.id.tvTotal);


        btnRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saborRemover != -1) {
                    if (saborRemover < adapterListView.getCount()) {
                        String saborRemovido = adapterListView.getItem(saborRemover);
                        adapterListView.remove(saborRemovido);
                    }
                    saborRemover = -1;
                }
                atualizarPedido();
            }
        });

        btnConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Salvo com Sucesso", Toast.LENGTH_SHORT).show();
                limparDados();
            }
        });

        btnLimpar = findViewById(R.id.btnLimpar);

        btnLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limparDados();
            }
        });


        cbBorda = findViewById(R.id.cbBorda);
        cbRefri = findViewById(R.id.cbRefri);


        cbBorda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    borda = "Sim";
                    valorBorda = 10.00;
                } else {
                    borda = "Não";
                    valorBorda = 0.00;
                }
                atualizarPedido();
                atualizarValorTotal();
            }
        });

        cbRefri.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    refri = "Sim";
                    valorRefri = 5.00;
                } else {
                    refri = "Não";
                    valorRefri = 0.00;
                }
                atualizarPedido();
                atualizarValorTotal();
            }
        });

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getSabores());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSabor.setAdapter(spinnerAdapter);

        spSabor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String saborSelecionado = parent.getItemAtPosition(position).toString();
                int quantidade = adapterListView.getCount();
                adicionarSabor(saborSelecionado, quantidade);
                atualizarPedido();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        adapterListView = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, saboresSelecionados);
        lvSabor.setAdapter(adapterListView);
        lvSabor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saborRemover = position;
            }
        });

        rbGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton != null && radioButton.getText() != null) {
                    tamanho = radioButton.getText().toString();
                    atualizarPedido();
                }
            }
        });


    }

    private List<String> getSabores() {
        List<String> sabores = new ArrayList<>();
        sabores.add("");
        sabores.add("Calabresa");
        sabores.add("Bacon");
        sabores.add("Camarão");
        sabores.add("Costela ao Vinho");
        sabores.add("Cinco Queijos");
        return sabores;
    }


    private void adicionarSabor(String saborSelecionado, int quantidade) {

        if (rbGrande.isChecked()) {
            if (quantidade < 4) {
                adapterListView.add(saborSelecionado);
            } else {
                Toast.makeText(MainActivity.this, "A pizza grande permite apenas 4 sabores.", Toast.LENGTH_SHORT).show();
            }

        } else if (rbMedia.isChecked()) {
            if (quantidade < 2) {
                adapterListView.add(saborSelecionado);
            } else {

                Toast.makeText(MainActivity.this, "A pizza média permite apenas 2 sabores.", Toast.LENGTH_SHORT).show();
            }
        } else if (rbPequena.isChecked()) {
            if (quantidade < 1) {
                adapterListView.add(saborSelecionado);
            } else {
                Toast.makeText(MainActivity.this, "A pizza grande permite apenas 1 sabores.", Toast.LENGTH_SHORT).show();

            }
        }

    }


    private void atualizarPedido() {
        StringBuilder pedido = new StringBuilder();
        pedido.append("\n Tamanho: ").append(tamanho).append("   R$" + valorTamanho).append("\n");
        pedido.append("Sabores: ").append(saboresSelecionados.toString()).append("\n");
        pedido.append("Com Borda: ").append(borda).append("   R$" + valorBorda).append("\n");
        pedido.append("Refri: ").append(refri).append("   R$" + valorRefri);
        tvPedido.setText(pedido.toString());

    }


    private void atualizarValorTotal() {

        double valorTotal = valorTamanho + valorBorda + valorRefri;

        tvTotal = findViewById(R.id.tvTotal);
        tvTotal.setText(String.valueOf("Valor Total:  R$" + valorTotal));
    }

    private void limparDados() {

        rbGroup.clearCheck();
        tamanho = "";
        valorTamanho = 0.00;


        saboresSelecionados.clear();
        adapterListView.notifyDataSetChanged();


        cbBorda.setChecked(false);
        borda = "Não";
        valorBorda = 0.00;


        cbRefri.setChecked(false);
        refri = "Não";
        valorRefri = 0.00;


        atualizarPedido();
        atualizarValorTotal();
    }


    public void selecionarOpcao(View view) {
        boolean isChecked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rbPequena:
                if (isChecked) {
                    tamanho = "Pequena";
                    valorTamanho = 20.00;
                    atualizarPedido();
                    atualizarValorTotal();
                }
                break;

            case R.id.rbMedia:
                if (isChecked) {
                    tamanho = "Média";
                    valorTamanho = 30.00;
                    atualizarPedido();
                    atualizarValorTotal();
                }
                break;

            case R.id.rbGrande:
                if (isChecked) {
                    tamanho = "Grande";
                    valorTamanho = 40.00;
                    atualizarPedido();
                    atualizarValorTotal();
                }
                break;
        }


    }
}

