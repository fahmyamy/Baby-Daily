package com.health.baby_daily.guide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.health.baby_daily.R;

public class VaccinePost extends AppCompatActivity {

    private Spinner vaccineSpinner;
    private TextView no_web_text;
    String[] list = { "Overview", "BCG", "HepB", "DTaP", "Hib", "IPV", "JE", "MMR",
                        "DT booster", "HPV", "TT"};

    private String htmlcontent;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine_post);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Vaccination");

        vaccineSpinner = findViewById(R.id.vaccineSpinner);
        vaccineSpinner.setVisibility(View.GONE);
        no_web_text = findViewById(R.id.no_web_text);
        no_web_text.setVisibility(View.GONE);

        //spinner content
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vaccineSpinner.setAdapter(adapter);

        String type = String.valueOf(vaccineSpinner.getSelectedItem());

        webView = findViewById(R.id.webView);

        /*vaccineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0){*/
                    webView.setVisibility(View.VISIBLE);
                    no_web_text.setVisibility(View.GONE);
                    htmlcontent = "<h2 class=\"tablepress-table-name tablepress-table-name-id-21\"><img class=\"alignnone wp-image-691 \" src=\"http://infomed.com.my/wp-content/uploads/2015/06/Vaccination-Schedule-Malaysia1.jpg\" alt=\"\" width=\"280\" height=\"220\" srcset=\"\" /></h2>\n" +
                            "<p>&nbsp;</p>\n" +
                            "<h2>Vaccination Schedule by the Ministry of Health Malaysia</h2>\n" +
                            "<table class=\"tftable\" border=\"1\">\n" +
                            "<tbody>\n" +
                            "<tr>\n" +
                            "<th>AGE</th>\n" +
                            "<th>VACCINATION</th>\n" +
                            "</tr>\n" +
                            "<tr>\n" +
                            "<td>Newborn</td>\n" +
                            "<td>Bacillus Calmette–Guérin (BCG)<br />\n" +
                            "1st dose: Hepatitis B (HepB)</td>\n" +
                            "</tr>\n" +
                            "<tr>\n" +
                            "<td>1 month</td>\n" +
                            "<td>2nd dose: Hepatitis B</td>\n" +
                            "</tr>\n" +
                            "<tr>\n" +
                            "<td>2 months</td>\n" +
                            "<td>1st dose:<br />\n" +
                            "&#8211; Diptheria, Tetanus, accellular Pertussis (DTaP)<br />\n" +
                            "&#8211; Haemophilus influenzae b (Hib)<br />\n" +
                            "&#8211; Inactivated Poliovirus (IPV)</td>\n" +
                            "</tr>\n" +
                            "<tr>\n" +
                            "<td>3 months</td>\n" +
                            "<td>2nd dose:<br />\n" +
                            "&#8211; DTaP<br />\n" +
                            "&#8211; Hib<br />\n" +
                            "&#8211; IPV</td>\n" +
                            "</tr>\n" +
                            "<tr>\n" +
                            "<td>5 months</td>\n" +
                            "<td>3rd dose:<br />\n" +
                            "&#8211; DTP<br />\n" +
                            "&#8211; Hib<br />\n" +
                            "&#8211; IPV</td>\n" +
                            "</tr>\n" +
                            "<tr>\n" +
                            "<td>6 months</td>\n" +
                            "<td>3rd dose: Hepatitis B<br />\n" +
                            "Measles (Sabah only)</td>\n" +
                            "</tr>\n" +
                            "<tr>\n" +
                            "<td>10 months</td>\n" +
                            "<td>1st Dose: Japanese Encephalitis (JE) (Sarawak only)</td>\n" +
                            "</tr>\n" +
                            "<tr>\n" +
                            "<td>12 months</td>\n" +
                            "<td>1st dose: Mumps, Measles, Rubella (MMR)<br />\n" +
                            "2nd dose: Japanese Encephalitis (Sarawak only)</td>\n" +
                            "</tr>\n" +
                            "<tr>\n" +
                            "<td>18 months</td>\n" +
                            "<td>4th dose:<br />\n" +
                            "&#8211; DTP<br />\n" +
                            "&#8211; Hib<br />\n" +
                            "&#8211; IPV<br />\n" +
                            "3rd dose: JE (Sarawak only)</td>\n" +
                            "</tr>\n" +
                            "<tr>\n" +
                            "<td>4 years old</td>\n" +
                            "<td>4th dose: JE (Sarawak only)</td>\n" +
                            "</tr>\n" +
                            "<tr>\n" +
                            "<td>7 years old</td>\n" +
                            "<td>&#8211; BCG (option only if no scar found)<br />\n" +
                            "&#8211; Diptheria, Tetanus  (DT booster)<br />\n" +
                            "&#8211; 2nd dose of MMR</td>\n" +
                            "</tr>\n" +
                            "<tr>\n" +
                            "<td>13 years old</td>\n" +
                            "<td>Human papillomavirus (HPV) with 3 doses within 6 months<br />\n" +
                            "(2nd dose 1 month after 1st dose, 3rd dose 6 months after 1st dose)</td>\n" +
                            "</tr>\n" +
                            "<tr>\n" +
                            "<td>15 years old</td>\n" +
                            "<td>Tetanus (TT)</td>\n" +
                            "</tr>\n" +
                            "</tbody>\n" +
                            "</table>\n" +
                            "<p><em>Source: <a href=\"https://kempas.malaysia.gov.my/en/citizen?articleId=266541&amp;subCatId=293775&amp;categoryId=126085\">Immunization Schedule from Ministry of Health Malaysia</a></em></p>\n" +
                            "<h6 style=\"text-align: right;\"><em> </em></h6>\n" +
                            "<h2>Optional Vaccines in Malaysia</h2>\n" +
                            "<p>Most paediatricians will recommend additional or optional vaccinations in addition to the ones mandated by the Ministry of Health. You can choose to administer them to your children, based on your doctor&#8217;s advice.</p>\n" +
                            "<ul>\n" +
                            "<li>&gt; 6 WEEKS : Rotavirus</li>\n" +
                            "<li>&gt; 2 MONTHS : Pneumococcal</li>\n" +
                            "<li>&gt; 6 MONTHS : Influenza</li>\n" +
                            "<li>&gt; 10 MONTHS : Hepatitis A</li>\n" +
                            "<li>&gt; 12 MONTHS :Chicken pox</li>\n" +
                            "</ul>\n" +
                            "<p>&nbsp;</p>\n" +
                            "<h2>How do vaccines work?</h2>\n" +
                            "<p>Vaccines work by introducing a weakened or dead form of infection, known as the antigen. In some vaccines, the antibody (the product of the immune system which helps the body fights antigen) is introduced. The introduction of the vaccine allows the body to create antibodies against a particular type of antigen / infection, which means upon actual exposure to the antigen, the body is able to fight the infection quickly and without succumbing to the illness.</p>\n" +
                            "<center><figure id=\"attachment_685\" style=\"width: 280px\" class=\"wp-caption alignnone\"><img class=\"wp-image-685 \" src=\"http://infomed.com.my/wp-content/uploads/2015/06/how-vaccines-workjpg-4124fe097a13d163.jpg\" alt=\"how-vaccines-workjpg-4124fe097a13d163\" width=\"280\" height=\"220\" /><figcaption class=\"wp-caption-text\">Source: M. Klingensmith, 2014</figcaption></figure></center>\n" +
                            "<p>&nbsp;</p>\n" +
                            "<p>You are protected from certain diseases for the first year after birth (thanks to your mother), which then fades away. Before vaccines, children used to die from diseases like whooping cough, polio and measles &#8211; all are greatly reduced to almost the point of zero in today&#8217;s developed world.<br />\n" +
                            "InfoMed advocates timely vaccination practice to keep your kids healthy and safe.</p>";

                    webView.setWebViewClient(new WebViewClient());
                    webView.loadDataWithBaseURL(null, htmlcontent, "text/html", "utf-8", null);
                /*}if (position == 1){
                    webView.setVisibility(View.VISIBLE);
                    no_web_text.setVisibility(View.GONE);
                    htmlcontent = "";

                    webView.setWebViewClient(new WebViewClient());
                    webView.loadDataWithBaseURL(null, htmlcontent, "text/html", "utf-8", null);
                }else if (position == 2){
                    webView.setVisibility(View.VISIBLE);
                    no_web_text.setVisibility(View.GONE);
                    htmlcontent = "";

                    webView.setWebViewClient(new WebViewClient());
                    webView.loadDataWithBaseURL(null, htmlcontent, "text/html", "utf-8", null);
                }else if (position == 3){
                    webView.setVisibility(View.VISIBLE);
                    no_web_text.setVisibility(View.GONE);
                    htmlcontent = "";

                    webView.setWebViewClient(new WebViewClient());
                    webView.loadDataWithBaseURL(null, htmlcontent, "text/html", "utf-8", null);
                }else if (position == 4){
                    webView.setVisibility(View.VISIBLE);
                    no_web_text.setVisibility(View.GONE);
                    htmlcontent = "";

                    webView.setWebViewClient(new WebViewClient());
                    webView.loadDataWithBaseURL(null, htmlcontent, "text/html", "utf-8", null);
                }else if (position == 5){
                    webView.setVisibility(View.VISIBLE);
                    no_web_text.setVisibility(View.GONE);
                    htmlcontent = "";

                    webView.setWebViewClient(new WebViewClient());
                    webView.loadDataWithBaseURL(null, htmlcontent, "text/html", "utf-8", null);
                }else if (position == 6){
                    webView.setVisibility(View.VISIBLE);
                    no_web_text.setVisibility(View.GONE);
                    htmlcontent = "";

                    webView.setWebViewClient(new WebViewClient());
                    webView.loadDataWithBaseURL(null, htmlcontent, "text/html", "utf-8", null);
                }else if (position == 7){
                    webView.setVisibility(View.VISIBLE);
                    no_web_text.setVisibility(View.GONE);
                    htmlcontent = "";

                    webView.setWebViewClient(new WebViewClient());
                    webView.loadDataWithBaseURL(null, htmlcontent, "text/html", "utf-8", null);
                }else if (position == 8){
                    webView.setVisibility(View.VISIBLE);
                    no_web_text.setVisibility(View.GONE);
                    htmlcontent = "";

                    webView.setWebViewClient(new WebViewClient());
                    webView.loadDataWithBaseURL(null, htmlcontent, "text/html", "utf-8", null);
                }else if (position == 9){
                    webView.setVisibility(View.VISIBLE);
                    no_web_text.setVisibility(View.GONE);
                    htmlcontent = "";

                    webView.setWebViewClient(new WebViewClient());
                    webView.loadDataWithBaseURL(null, htmlcontent, "text/html", "utf-8", null);
                }else if (position == 10){
                    webView.setVisibility(View.VISIBLE);
                    no_web_text.setVisibility(View.GONE);
                    htmlcontent = "";

                    webView.setWebViewClient(new WebViewClient());
                    webView.loadDataWithBaseURL(null, htmlcontent, "text/html", "utf-8", null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //WebSettings webSettings = webView.getSettings();
        //webSettings.setJavaScriptEnabled(true);*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
