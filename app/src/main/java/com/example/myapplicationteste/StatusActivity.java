package com.example.myapplicationteste;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class StatusActivity extends AppCompatActivity {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    // IDs dos ImageViews que representam os círculos de status
    private ImageView statusCredencial;
    private ImageView statusReciclagem;
    private ImageView statusAvsec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        // 1. Inicializa os ImageViews
        statusCredencial = findViewById(R.id.status_credencial);
        statusReciclagem = findViewById(R.id.status_reciclagem);
        statusAvsec = findViewById(R.id.status_avsec);

        // 2. Obtém as datas (Simulando a leitura do TextView do layout)
        // OBS: Em uma aplicação real, você leria essas datas de um banco de dados ou API.
        String dataCredencialStr = ((TextView) findViewById(R.id.data_credencial)).getText().toString();
        String dataReciclagemStr = ((TextView) findViewById(R.id.data_reciclagem)).getText().toString();
        String dataAvsecStr = ((TextView) findViewById(R.id.data_avsec)).getText().toString();

        // 3. Aplica a lógica de status
        updateStatusColor(dataCredencialStr, statusCredencial);
        updateStatusColor(dataReciclagemStr, statusReciclagem);
        updateStatusColor(dataAvsecStr, statusAvsec);
    }

    /**
     * Define a cor do indicador de status com base na proximidade da data de vencimento.
     * @param dateString A data de vencimento no formato "dd/MM/yyyy".
     * @param statusView O ImageView (círculo) a ser atualizado.
     */
    private void updateStatusColor(String dateString, ImageView statusView) {
        try {
            Date expirationDate = dateFormat.parse(dateString);
            Date currentDate = new Date(); // Data e hora atual

            // Calcula a diferença em milissegundos
            long diff = expirationDate.getTime() - currentDate.getTime();

            // Converte a diferença para dias
            long daysRemaining = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            int drawableResId; // Recurso drawable a ser aplicado

            if (daysRemaining < 0) {
                // Vencida (Vermelho) - Dias restantes < 0
                drawableResId = R.drawable.ic_status_circle_red;

            } else if (daysRemaining <= 14) {
                // 2 Semanas antes (Laranja Escuro) - Dias restantes <= 14
                drawableResId = R.drawable.ic_status_circle_orange;

            } else if (daysRemaining <= 30) {
                // 1 Mês a 2 Semanas antes (Amarelo) - Dias restantes <= 30
                drawableResId = R.drawable.ic_status_circle_yellow;

            } else {
                // Acima de 1 Mês (Verde) - Dias restantes > 30
                drawableResId = R.drawable.ic_status_circle_green;
            }

            // Aplica o drawable (cor) ao ImageView
            Drawable drawable = ContextCompat.getDrawable(this, drawableResId);
            statusView.setBackground(drawable);

        } catch (ParseException e) {
            // Se a data for inválida, loga o erro ou define um status padrão.
            e.printStackTrace();
            statusView.setBackgroundResource(R.drawable.ic_status_default);
        }
    }
}