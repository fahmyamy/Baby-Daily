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

public class FirstAid extends AppCompatActivity {

    private Spinner firstAidSpinner;
    private TextView no_web_text;
    String[] list = { "Select...", "Choked", "CPR", "Fever", "Vomit"};
    private String htmlcontent;

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("First Aid");

        firstAidSpinner = findViewById(R.id.firstAidSpinner);
        no_web_text = findViewById(R.id.no_web_text);

        //spinner content
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        firstAidSpinner.setAdapter(adapter);

        String type = String.valueOf(firstAidSpinner.getSelectedItem());

        webView = findViewById(R.id.webView);

        firstAidSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0){
                    webView.setVisibility(View.GONE);
                    no_web_text.setVisibility(View.VISIBLE);
                }if (position == 1){
                    webView.setVisibility(View.VISIBLE);
                    no_web_text.setVisibility(View.GONE);
                    htmlcontent = "<h2>Choking</h2>\n" +
                            "<p><strong>Step 1: Assess the situation quickly.</strong><br/>If a baby is suddenly unable to cry or cough, something is probably blocking her airway, and you'll need to help her get it out. She may make odd noises or no sound at all while opening her mouth. Her skin may turn red or blue.</p>\n" +
                            "<p>If she's coughing or gagging, it means her airway is only partially blocked. If that's the case, let her continue to cough. Coughing is the most effective way to dislodge a blockage.</p>\n" +
                            "<p>If the baby isn't able to cough up the object, ask someone to call 911 or the local emergency number while you begin back blows and chest thrusts (see step 2, below).</p>\n" +
                            "<p>If you're alone with the baby, give two minutes of care, then call 911.</p>\n" +
                            "<p>On the other hand, if you suspect that the baby's airway is closed because her throat has swollen shut, call 911 immediately. She may be having an allergic reaction &#8211; to food or to an insect bite, for example &#8211; or she may have an illness, such as croup.</p>\n" +
                            "<p>Also call 911 right away if the baby is at high risk for heart problems or if you witnessed the baby suddenly collapse.</p>\n" +
                            "<div class=\"embeddedImage\">\n" +
                            "<img height=\"220\" src=\"file:///android_res/drawable/step1choked.jpg\" width=\"280\"/></div>\n" +
                            "<p><strong>Step 2: Try to dislodge the object with back blows and chest thrusts.</strong><br/>" +
                            "<em>First do back blows</em><br/>If a baby is conscious but can't cough, cry, or breathe and you believe something is trapped in his airway, carefully position him face-up on one forearm, cradling the back of his head with that hand.</p>\n" +
                            "<p>Place the other hand and forearm on his front. He is now sandwiched between your forearms.</p>\n" +
                            "<p>Use your thumb and fingers to hold his jaw and turn him over so that he's facedown along your forearm. Lower your arm onto your thigh so that the baby's head is lower than his chest.</p>\n" +
                            "<p>Using the heel of your hand, deliver five firm and distinct back blows between the baby's shoulder blades to try to dislodge the object. Maintain support of his head and neck by firmly holding his jaw between your thumb and forefinger.</p>\n" +
                            "<p>If the object does not come out, place your free hand (the one that had been delivering the back blows) on the back of the baby's head with your arm along his spine. Carefully turn him over while keeping your other hand and forearm on his front.</p>\n" +
                            "<p><em>Then do chest thrusts</em><br/>Use your thumb and fingers to hold the baby's jaw while sandwiching him between your forearms to support his head and neck. Lower your arm that is supporting his back onto your thigh, still keeping the baby's head lower than the rest of his body.</p>\n" +
                            "<p>Place the pads of two or three fingers in the center of the baby's chest, just below an imaginary line running between his nipples. To do a chest thrust, push straight down on the chest about 1 1/2 inches. Then allow the chest to come back to its normal position.</p>\n" +
                            "<p>Do five chest thrusts. Keep your fingers in contact with the baby's breastbone. The chest thrusts should be smooth, not jerky.</p>\n" +
                            "<p><em>Repeat back blows and chest thrusts</em><br/>Continue alternating five back blows and five chest thrusts until the object is forced out or the baby starts to cough forcefully, cry, breathe, or becomes unresponsive. If he's coughing, let him try to cough up the object.</p>\n" +
                            "<p><em>If the baby becomes unresponsive</em><br/>If a baby who is choking on something becomes unconscious, lower the baby to the ground and start CPR (see below). After each set of compressions and before attempting rescue breaths, open the baby's mouth, look for the blockage and remove it if you can.</p>\n" +
                            "<p>Never put your finger in the baby's mouth unless you actually see a blockage. If you can't see it and you put your finger in his mouth, you might accidentally push the blockage deeper into his throat. If you can see a blockage, remove it with your little finger.</p>\n" +
                            "<p>Continue the sequence until the child revives or help arrives.</p>\n" +
                            "<br/><br/><h5>Sources: <a href=\"https://www.babycenter.com/0_infant-first-aid-for-choking-and-cpr-an-illustrated-guide_9298.bc\">Baby Center</a>";

                    webView.setWebViewClient(new WebViewClient());
                    webView.loadDataWithBaseURL(null, htmlcontent, "text/html", "utf-8", null);
                }else if (position == 2){
                    webView.setVisibility(View.VISIBLE);
                    no_web_text.setVisibility(View.GONE);
                    htmlcontent = "</a><h2>How to perform CPR</h2>\n" +
                            "<p><strong>What is CPR?</strong><br/>CPR stands for cardiopulmonary resuscitation. This is the lifesaving measure you can take to save a baby who shows no signs of life, meaning he is unconscious and not breathing.</p>\n" +
                            "<p>CPR uses chest compressions and rescue breaths to make oxygen-rich blood circulate through the brain and other vital organs until the child revives or emergency medical personnel arrive. Keeping oxygenated blood circulating helps prevent brain damage &#8211; which can occur within a few minutes &#8211; and death.</p>\n" +
                            "<p>Infant CPR isn't hard to do. Follow these steps:</p>\n" +
                            "<p><strong>Step 1: Verify that the infant is unresponsive and not breathing.</strong><br/>Shout to get the infant's attention, using her name. If she doesn't respond, tap the bottom of her foot and shout again while checking for normal breathing. (Look to see whether her chest is rising, and listen for breathing sounds.)</p>\n" +
                            "<p>If the infant doesn't respond and is not breathing or is gasping, have someone call 911 or the local emergency number. (If you're alone with the baby, give two minutes of care as described below, then call 911 yourself.)</p>\n" +
                            "<p>Swiftly but gently place the baby on her back on a firm, flat surface. Stand or kneel next to her.</p>\n" +
                            "<p>Make sure she isn't <a href=\"/0_severe-bleeding_11234.bc\">bleeding</a> severely. If she is, take measures to stop the bleeding by applying pressure to the area. Don't administer CPR until the bleeding is under control.</p>\n" +
                            "<div class=\"embeddedImage\">\n" +
                            "<img height=\"220\" src=\"file:///android_res/drawable/cpr_a.jpg\" width=\"280\" /></div>\n" +
                            "<p><strong>Step 2: Give 30 chest compressions.</strong><br/>Place one hand on the infant's forehead. Place the pads of two fingers in the center of his chest, just below the nipple line. Compress the chest by pushing straight down about 1 1/2 inches, and then let the chest return to its normal position. Push hard and push fast. Compressions should be smooth, not jerky.</p>\n" +
                            "<p>Give compressions at the rate of two per second. Count out loud: \"One and two and three and...\", pushing down as you say the number and coming up as you say \"and.\" (The song <em>Staying Alive</em> has the rhythm you're shooting for.)</p>\n" +
                            "<p><strong>Step 3: Give two rescue breaths</strong>.<br/>Note: If you're uncomfortable giving rescue breaths, giving chest compressions only is better than nothing.</p>\n" +
                            "<div class=\"embeddedImage\">\n" +
                            "<img height=\"220\" src=\"file:///android_res/drawable/cpr_b.jpg\" width=\"280\" /></div>\n" +
                            "<p>Open the airway by putting one hand on the baby's forehead and two fingers on his chin and tilting his head back to a neutral position. Make a complete seal over the baby's nose and mouth with your mouth.</p>\n" +
                            "<p>Take a normal breath and blow into the baby's nose and mouth for about one second, looking to see if the chest rises. If the chest does not rise, retilt the head and ensure a proper seal before giving a second rescue breath.</p>\n" +
                            "<p>If the baby's chest doesn't rise, his airway is blocked. Open the baby's mouth, look for the blockage and remove it if you can. Continue to check the mouth for an object after each set of compressions until the baby's chest rises as rescue breaths go in.</p>\n" +
                            "<p><strong>Continue giving sets</strong> of 30 compressions and two rescue breaths until:</p>\n" +
                            "<ul>\n" +
                            "<li>You notice an obvious sign of life.</li>\n" +
                            "<li>An AED (automated external defibrillator) is ready to use</li>\n" +
                            "<li>You have performed approximately 2 minutes of CPR (5 sets of compressions and rescue breaths) and another person is available to take over compressions.</li>\n" +
                            "<li>You have performed approximately 2 minutes of CPR (5 sets of compressions and rescue breaths), you are alone with the baby, and you need to call 911 or the designated emergency number.</li>\n" +
                            "<li>EMS personnel take over.</li>\n" +
                            "<li>You are too tired to continue.</li>\n" +
                            "<li>The scene becomes unsafe.</li>\n" +
                            "</ul>\n" +
                            "<p>Even if the baby seems fine by the time help arrives, a doctor will need to check her to make sure that her airway is completely clear and she hasn't sustained any internal injuries.</p>\n" +
                            "<br/><br/><h5>Sources: <a href=\"https://www.babycenter.com/0_infant-first-aid-for-choking-and-cpr-an-illustrated-guide_9298.bc\">Baby Center</a>";

                    webView.setWebViewClient(new WebViewClient());
                    webView.loadDataWithBaseURL(null, htmlcontent, "text/html", "utf-8", null);
                }else if (position == 3){
                    webView.setVisibility(View.VISIBLE);
                    no_web_text.setVisibility(View.GONE);
                    htmlcontent = "<section id=\"overview\" data-rowname=\"Overview\" data-header-style=\"\" class=\"row fw-row\">\n" +
                            "<div class=\"spb-row-container spb-row-full-width-contained col-sm-12   text-standard\" data-v-center=\"false\" data-top-style=\"none\" data-bottom-style=\"none\" style=\"margin-top:0px;margin-bottom:0px;\">\n" +
                            "<div class=\"spb_content_element clearfix\" style=\"padding-top:0px;padding-bottom:0px;\">\n" +
                            "<section class=\"container\"><div class=\"row\">\n" +
                            "<div class=\"spb_content_element col-sm-12 spb_text_column\">\n" +
                            "<div class=\"spb-asset-content\" style=\"padding-top:0%;padding-bottom:0%;padding-left:0%;padding-right:0%;\">\n" +
                            "<h2>How Do I Know If My Child Has A Fever?</h2>\n" +
                            "<h3>What is a normal temperature?</h3>\n" +
                            "<p>A normal temperature is about 98.6°F (37°C) when taken orally (in your child&#8217;s mouth) and 99.6°F (37.5°C) when taken rectally (in your child&#8217;s bottom). Many doctors define a fever as an oral temperature above 99.5°F (37.5°C) or a rectal temperature above 100.4°F (38°C).</p>\n" +
                            "<h3>How should I take my child&#8217;s temperature?</h3>\n" +
                            "<p>You can get the most accurate temperature by taking his or her temperature rectally. But in a child older than 3 months of age, it&#8217;s fine to take it orally unless your doctor directs otherwise. Use a digital thermometer. Do not use a mercury thermometer. Mercury is an environmental toxin (poison), and you don&#8217;t want to risk exposing your family to it.</p>\n" +
                            "<ul>\n" +
                            "<li>Be sure to label your rectal thermometer so that it isn&#8217;t accidentally used in your child&#8217;s mouth.</li>\n" +
                            "<li>Before taking your child&#8217;s temperature, clean the thermometer in lukewarm soapy water and rinse it well with cool water.</li>\n" +
                            "<li>If you&#8217;re taking your child&#8217;s temperature orally, wait at least 20 minutes after your child finishes eating or drinking hot or cold foods and beverages to take his or her temperature.</li>\n" +
                            "<li>Don&#8217;t bundle your baby or child up too tightly before taking his or her temperature.</li>\n" +
                            "<li>Don&#8217;t take your child&#8217;s temperature right after he or she has had a bath.</li>\n" +
                            "<li>Never leave your child alone while using a thermometer.</li>\n" +
                            "<li>After you&#8217;re done using a thermometer, clean it with rubbing alcohol or wash it in cool, soapy water.</li>\n" +
                            "</ul>\n" +
                            "<h3>Taking your child&#8217;s temperature rectally</h3>\n" +
                            "<p>If you&#8217;re taking your child&#8217;s temperature rectally, place him or her belly-down across your lap. Coat the tip of the thermometer with petroleum jelly (brand name: Vaseline) and insert it half an inch into the rectum. Stop if you feel any resistance. Hold the thermometer still and do not let go. When the thermometer beeps, remove it and check the digital reading.</p>\n" +
                            "<h3>Taking your child&#8217;s temperature orally</h3>\n" +
                            "<p>If you&#8217;re taking your child&#8217;s temperature orally, place the end of the thermometer under his or her tongue, towards the back of the mouth. Have your child close his or her lips on the thermometer. Tell your child not to bite down or talk. When the thermometer beeps, remove it and check the digital reading.</p>\n" +
                            "</div>\n" +
                            "</div> </div></section>\n" +
                            "</div>\n" +
                            "</div></section>\n" +
                            "<section id=\"symptoms\" data-rowname=\"Symptoms\" data-header-style=\"\" class=\"row fw-row\">\n" +
                            "<div class=\"spb-row-container spb-row-full-width-contained col-sm-12   text-standard\" data-v-center=\"false\" data-top-style=\"none\" data-bottom-style=\"none\" style=\"margin-top:0px;margin-bottom:0px;\">\n" +
                            "<div class=\"spb_content_element clearfix\" style=\"padding-top:0px;padding-bottom:0px;\">\n" +
                            "<section class=\"container\"><div class=\"row\">\n" +
                            "<div class=\"spb_content_element col-sm-12 spb_text_column\">\n" +
                            "<div class=\"spb-asset-content\" style=\"padding-top:0%;padding-bottom:0%;padding-left:0%;padding-right:0%;\">\n" +
                            "<h2>When Should I Call My Doctor?</h2>\n" +
                            "<h3>Symptoms</h3>\n" +
                            "<p>If your child has any of the following warning signs, call your family doctor right away.</p>\n" +
                            "<ul>\n" +
                            "<li>Constant vomiting or diarrhea</li>\n" +
                            "<li>Dry mouth</li>\n" +
                            "<li>Earache or pulling at ears</li>\n" +
                            "<li>Fever comes and goes over several days</li>\n" +
                            "<li>High-pitched crying</li>\n" +
                            "<li>Irritability</li>\n" +
                            "<li>No appetite</li>\n" +
                            "<li>Pale appearance</li>\n" +
                            "<li>Seizures</li>\n" +
                            "<li>Severe headache</li>\n" +
                            "<li>Skin rash</li>\n" +
                            "<li>Sore or swollen joints</li>\n" +
                            "<li>Sore throat</li>\n" +
                            "<li>Stiff neck</li>\n" +
                            "<li>Stomach pain</li>\n" +
                            "<li>Swelling of the soft spot on an infant&#8217;s head</li>\n" +
                            "<li>Unresponsiveness or limpness</li>\n" +
                            "<li>Wheezing or problems breathing</li>\n" +
                            "<li>Whimpering</li>\n" +
                            "</ul>\n" +
                            "<h3>If your child is:</h3>\n" +
                            "<p>Younger than 3 months of age, call your doctor right away if your baby&#8217;s rectal temperature is 100.4°F (38°C) or higher. Call your doctor even if your child doesn&#8217;t seem sick. Babies this young can get very sick quickly.</p>\n" +
                            "<p>Three months of age to 6 months of age, call your doctor if your baby has a temperature of 102°F (38.8°C) or higher, even if your baby doesn&#8217;t seem sick.</p>\n" +
                            "<p>Six months of age and older and has a fever of 102°F (38.8°C) to 102.9°F (39.4°C), watch how he or she acts. Call your doctor if the fever rises or lasts for more than 2 days.</p>\n" +
                            "<p>Six months of age and older and has a fever of 103°F (39.4°C) or higher, call your doctor even if your child seems to feel fine.</p>\n" +
                            "</div>\n" +
                            "</div> </div></section>\n" +
                            "</div>\n" +
                            "</div></section>\n" +
                            "<section id=\"treatment\" data-rowname=\"Treatment\" data-header-style=\"\" class=\"row fw-row\">\n" +
                            "<div class=\"spb-row-container spb-row-full-width-contained col-sm-12   text-standard\" data-v-center=\"false\" data-top-style=\"none\" data-bottom-style=\"none\" style=\"margin-top:0px;margin-bottom:0px;\">\n" +
                            "<div class=\"spb_content_element clearfix\" style=\"padding-top:0px;padding-bottom:0px;\">\n" +
                            "<section class=\"container\"><div class=\"row\">\n" +
                            "<div class=\"spb_content_element col-sm-12 spb_text_column\">\n" +
                            "<div class=\"spb-asset-content\" style=\"padding-top:0%;padding-bottom:0%;padding-left:0%;padding-right:0%;\">\n" +
                            "<h2>How Can I Treat My Child&#8217;s Fever?</h2>\n" +
                            "<h3>Should I try to lower my child&#8217;s fever?</h3>\n" +
                            "<p>Fevers are a sign that the body is fighting germs that cause infection. If your child is between 3 months of age and 3 years of age and has a low-grade fever (up to 102°F [38.8°C]), you may want to avoid giving him or her medicine. If your child is achy and fussy, and his or her temperature is above 102°F (38.8°C), you may want to give him or her some medicine.</p>\n" +
                            "<p>If your baby is younger than 3 months of age and has a rectal temperature of 100.4°F (38°C) or higher, call the doctor or go to the emergency room right away. A fever can be a sign of a serious infection in young babies.</p>\n" +
                            "<h3>What kind of medicine should I give my child, and how much?</h3>\n" +
                            "<p>Do not give any medicine to babies who are younger than 2 months of age without talking to your doctor first.</p>\n" +
                            "<p>Acetaminophen (one brand name: Children&#8217;s or Infants&#8217; Tylenol) relieves pain and lowers fever. Check the package label or ask your doctor about the correct dosage for your child. The correct dosage depends on your child&#8217;s weight and age.</p>\n" +
                            "<p>Ibuprofen is another medicine that can be used to lower a fever in children older than 6 months of age. Talk to your doctor before giving ibuprofen (two brand names: Children&#8217;s Advil, Children&#8217;s Motrin) to your child. Your doctor will tell you the correct dose for your child.</p>\n" +
                            "<h3>Can I give my child aspirin to lower his or her fever?</h3>\n" +
                            "<p>No. In rare cases, aspirin can cause Reye&#8217;s syndrome in children. Reye&#8217;s syndrome is a serious illness that can lead to death. Doctors recommend that parents should not give aspirin to children younger than 18 years of age.</p>\n" +
                            "<h3>What else can I do to help my child feel better?</h3>\n" +
                            "<ul>\n" +
                            "<li>Give your child plenty of fluids to drink to prevent dehydration (not enough fluid in the body) and help the body cool itself. Water, clear soups, popsicles and flavored gelatin are good choices.</li>\n" +
                            "<li>If your child is getting enough fluids, don&#8217;t force him or her to eat if he or she doesn&#8217;t feel like it.</li>\n" +
                            "<li>Make sure your child gets plenty of rest.</li>\n" +
                            "<li>Keep the room temperature at about 70°F to 74°F.</li>\n" +
                            "<li>Dress your child in light cotton pajamas. Overdressing can trap body heat and cause your child&#8217;s temperature to rise.</li>\n" +
                            "<li>If your child has chills, give him or her an extra blanket. Remove it when the chills stop.</li>\n" +
                            "</ul>\n" +
                            "<h3>Will a bath help lower my child&#8217;s fever?</h3>\n" +
                            "<p>Giving your child acetaminophen and a lukewarm bath may help lower his or her fever. Give the acetaminophen before the bath. If the bath is given without medicine, your child may start shivering as his or her body tries to raise its temperature again. This may make your child feel worse. Never use rubbing alcohol or cold water for baths.</p>\n" +
                            "<h3>Tips on giving medicine</h3>\n" +
                            "<ul>\n" +
                            "<li>Don&#8217;t give more than 5 doses in 1 day.</li>\n" +
                            "<li>Don&#8217;t give a baby younger than 2 months of age any medicine unless your doctor tells you to.</li>\n" +
                            "<li>Read package labels carefully. Make sure you are giving your child the right amount of medicine.</li>\n" +
                            "<li>For liquid medicines, use a special liquid measuring device to be sure you give the right dose. Get one at your drug store or ask your pharmacist. An ordinary kitchen teaspoon may not hold the right amount of medicine.</li>\n" +
                            "</ul>\n" +
                            "</div>\n" +
                            "</div> </div></section>\n" +
                            "</div>\n" +
                            "</div></section>\n" +
                            "<section id=\"questions\" data-rowname=\"Questions\" data-header-style=\"\" class=\"row fw-row\">\n" +
                            "<div class=\"spb-row-container spb-row-full-width-contained col-sm-12   text-standard\" data-v-center=\"false\" data-top-style=\"none\" data-bottom-style=\"none\" style=\"margin-top:0px;margin-bottom:0px;\">\n" +
                            "<div class=\"spb_content_element clearfix\" style=\"padding-top:0px;padding-bottom:0px;\">\n" +
                            "<section class=\"container\"><div class=\"row\">\n" +
                            "<div class=\"spb_content_element col-sm-12 spb_text_column\">\n" +
                            "<div class=\"spb-asset-content\" style=\"padding-top:0%;padding-bottom:0%;padding-left:0%;padding-right:0%;\">\n" +
                            "<h2>More Things To Consider</h2>\n" +
                            "<h3>Questions to Ask Your Doctor</h3>\n" +
                            "<ul>\n" +
                            "<li>Is there any medicine I can give my child?</li>\n" +
                            "<li>What is the correct dose of medicine for my child?</li>\n" +
                            "<li>If my child&#8217;s fever goes up suddenly, should I take him/her to the emergency room?</li>\n" +
                            "<li>How long should I wait to call the doctor if my child&#8217;s fever doesn&#8217;t go away?</li>\n" +
                            "<li>What is the easiest way to take my child&#8217;s temperature?</li>\n" +
                            "<li>How can I make my child comfortable during the fever?</li>\n" +
                            "</ul>\n" +
                            "</div>\n" +
                            "</div> </div></section>\n" +
                            "</div>\n" +
                            "</div></section>\n" +
                            "</div> </div>" +
                            "<br/><br/><h5>Sources: <a href=\"https://familydoctor.org/condition/fever-in-infants-and-children/\">familydoctor.org</a>";

                    webView.setWebViewClient(new WebViewClient());
                    webView.loadDataWithBaseURL(null, htmlcontent, "text/html", "utf-8", null);
                }else if (position == 4){
                    webView.setVisibility(View.VISIBLE);
                    no_web_text.setVisibility(View.GONE);
                    htmlcontent = "<h2>Why Babies Vomit—and What You Can Do About It</h2>\n" +
                            "<div class='body-atoms body-content'>\n" +
                            "<p>There’s nothing less fun than throwing up, except maybe when it’s baby who’s the one suffering. Not only is she miserable, but you’re also probably worried sick, wondering what to do and how to care for her. As unpleasant as it is, witnessing a baby throwing up is something all parents go through—and more than just once. For the most part, baby vomiting isn’t a major cause for concern. Read on to learn the difference between baby spit-up and vomit and what to do for a baby throwing up.</p>\n" +
                            "<p><strong>In this article:</strong><br>\n" +
                            "<a href=\"#1\">Baby vomit vs spit-up</a><br>\n" +
                            "<a href=\"#2\">Why do babies throw up?</a><br>\n" +
                            "<a href=\"#3\">Why Babies Vomit—and What You Can Do About It</a><br>\n" +
                            "<a href=\"#4\">What to give baby after vomiting</a><br>\n" +
                            "<a href=\"#5\">How to stop baby from throwing up</a></p>\n" +
                            "<p><a class='anchor-tag' name='1'></a></p>\n" +
                            "<h2>Baby Vomit vs Spit-Up</h2>\n" +
                            "<p>It can be tough to tell the difference between baby vomit vs <a href=\"https://www.thebump.com/a/baby-spit-up\">spit-up</a> at first—especially if baby is on a milk-only diet, since infant vomit and spit-up look pretty much the same at that point. Once baby starts solid foods, the difference will be more clear: Vomit will often contain regurgitated food and therefore have a thicker consistency. Plus, babies tend to spit up way less frequently once they’re eating solids. But until then, the clue to telling apart baby vomit vs spit-up may lie in baby’s mood immediately after.</p>\n" +
                            "<p>“Baby vomiting is the forceful expulsion of the stomach’s contents. The baby is usually irritable and upset by it,” says Anthony M. Loizides, director of the Aerodigestive Center, a division of pediatric gastroenterology and nutrition at Children&#39;s Hospital at Montefiore in New York City. “Spitting up usually looks like the stomach contents are ‘pouring out’ of the mouth, and the baby isn’t bothered by it and instead goes about his business.”</p>\n" +
                            "<p><a class='anchor-tag' name='2'></a></p>\n" +
                            "<h2>Why Do Babies Throw Up?</h2>\n" +
                            "<p>Babies throw up for a number of reasons, and although a stomach bug is often to blame, that’s not always the case. Here are some other things that can lead to baby vomiting:</p>\n" +
                            "<p>• <strong>A milk or food allergy.</strong> “[If the child has a] milk protein allergy, baby vomiting can be related to the exposure to milk proteins either via breastmilk or formula,” says Melanie Greifer, MD, a pediatric gastroenterologist at Hassenfeld Children’s Hospital at NYU Langone in New York City. If baby is throwing up formula, consider switching to a soy-based formula or a hydrolyzed formula, which breaks down the milk particles and makes them easier for baby to digest. Similarly, baby throwing up after breastfeeding can indicate that he’s allergic to something in your diet. If the problem continues, contact a pediatrician or lactation consultant, who can help you begin an elimination diet.</p>\n" +
                            "<p>• <strong>Eating too much too quickly.</strong> Wondering what causes baby vomiting after eating? Both vomiting and spitting up can occur in babies who need to be burped more frequently or are being fed too much milk (either via breast or bottle) to comfortably fit in baby’s small stomach, Greifer says.</p>\n" +
                            "<p>• <strong>A triggered gag reflex.</strong> If baby has a sensitive gag reflex, he’ll be more likely to throw up after a coughing spell or even after tasting food or medication that he really doesn’t like. In these cases, you’ll notice baby coughing and throwing up immediately after swallowing.</p>\n" +
                            "<p>• <strong>Motion sickness.</strong> Just like adults, babies can suffer from motion sickness. This may be the culprit behind baby throwing up if you or your partner suffers from motion sickness as well.</p>\n" +
                            "<h3>Baby throwing up at night</h3>\n" +
                            "<p>Parenting comes with its share of Murphy’s Law moments. Does it seem like the second you hit the sack, baby starts throwing up? There’s actually a physiological reason for baby throwing up at night, especially in younger babies who are still exclusively formula- or breastfed[CP2] .</p>\n" +
                            "<p>“When a baby lies down at night on her back (the recommended position by the American Academy of Pediatrics), anatomically the position of the stomach is higher than the esophagus, and therefore it’s more likely that a baby may spit up,” Loizides says. Plus, the valve between the stomach and esophagus relaxes more while baby’s sleeping, leading to a baby throwing up more often at night than during the day.</p>\n" +
                            "<p>If baby suffers from <a href=\"https://www.thebump.com/a/reflux-baby\">reflux</a> and is having more spit up or vomiting episodes at night, you can incline the crib mattress using a <a href=\"https://www.amazon.com/EasyZzs-Universal-Positioner-Waterproof-Hypoallergenic/dp/B071X1D5ZH/ref=sr_1_2?tag=thebump-generic-20\">crib wedge</a> designed specifically for that purpose, or you can simply roll a beach towel and place it under the mattress.</p>\n" +
                            "<p><a class='anchor-tag' name='3'></a></p>\n" +
                            "<h2>When to Be Concerned About Baby Throwing Up</h2>\n" +
                            "<p>Although a sick baby is never easy to deal with, rest assured that most of the time, when baby’s vomiting with no fever, it’s pretty harmless and likely to pass quickly. There are, however, some situations where baby vomiting is a sign of something more serious. Here are some red flag situations that warrant a professional opinion:</p>\n" +
                            "<p>• <strong>Baby throwing up clear liquid.</strong> A possible cause of baby vomiting is a condition called pyloric stenosis, in which the valve between the stomach and small intestines thickens and prevents food from passing through. The onset of this condition is typically between 2 to 3 weeks of age and often begins as a typical episode of baby throwing up clear liquid after a feeding—but it quickly escalates in both force and number of episodes. Pyloric stenosis is also one of the main reasons for projectile vomiting in babies. The good news is that this condition can be resolved with a simple surgical procedure.</p>\n" +
                            "<p>• <strong>Baby throwing up blood.</strong> If you’ve been through this, you know how terrifying it can be. But if you’re seeing traces of blood in baby’s vomit, it could simply be a sign of a small tear in baby’s esophagus as a result of forceful vomiting or coughing. It may sound scary, but small tears like these heal pretty quickly on their own and don’t call for medical attention. If the tear is significant, you’ll see a large volume of fresh red blood in the vomit—and in that case, head to the emergency room. Regardless of how much red you see when baby is throwing up blood, let your pediatrician know. If baby keeps throwing up milk and the vomit contains traces of blood, it could be a sign of a milk allergy, which you’ll want to resolve as soon as possible.</p>\n" +
                            "<p>• <strong>Baby vomiting yellow or green bile.</strong> If baby is vomiting yellow liquid, it could be a sign of an obstruction in the bowel or intestines—or it could simply indicate that there’s nothing else in baby’s stomach to throw up, so a bit of bile comes up. If you notice baby producing a small amount of frothy yellow vomit at the tail end of a nasty stomach bug with repeated vomiting, give the pediatrician a call. However, if you see baby throwing up <em>green</em> vomit, head to the nearest emergency room, since that’s a sign of a surgical emergency and calls for immediate medical care.</p>\n" +
                            "<p>• <strong>Baby throwing up with high fever and crying.</strong> If baby is throwing up, has a high fever and seems unwell, this should warrant a trip to the pediatrician or even the emergency room. These symptoms are typically associated with a bacterial infection, like meningitis, and can sometimes be indicative of appendicitis as well.</p>\n" +
                            "<p><a class='anchor-tag' name='4'></a></p>\n" +
                            "<h2>What to Give Baby After Vomiting</h2>\n" +
                            "<p>If baby is experiencing a bout of repeated vomiting, don’t be too quick to give him anything to eat or drink, as you’ll likely see it again very soon. Give baby’s system some forced rest during this period, and watch for signs of dehydration, like reduced amount of tears, sunken eyes and a decrease in wet diapers. Most tummy troubles resolve themselves before baby becomes dehydrated—but if you do spot the telltale signs of dehydration and the vomiting isn’t letting up, call the pediatrician, who may give baby IV fluids.</p>\n" +
                            "<p>As soon as the episodes begin to lessen, it’s okay to begin offering liquids in small doses. If it’s a breastfed newborn vomiting, it’s best to offer breast milk, since it’s generally easy to digest and therefore well tolerated at this stage. Offer more frequent feedings than usual, but keep the sessions shorter to prevent baby’s tummy from becoming too full. If baby is formula-fed, it’s okay to offer formula: Start with a tablespoon or two at first and offer more if baby’s able to keep it down. Babies 6 months or older can be given a tablespoon or two of water, but avoid electrolyte solution like Pedialyte[CP4] before baby reaches her first birthday, unless your pediatrician advises otherwise.</p>\n" +
                            "<p><a class='anchor-tag' name='5'></a></p>\n" +
                            "<h2>How to Stop Baby from Throwing Up</h2>\n" +
                            "<p>If you’re wondering how to stop baby from throwing up, we totally understand! But as tempting as it is, you shouldn’t attempt to stop baby vomiting. After all, it’s a sign that there’s something in the body that needs to be removed, whether it’s caused by an overly full stomach or a toxin.</p>" +
                            "<br/><br/><h5>Sources: <a href=\"https://www.thebump.com/a/baby-throwing-up\">thebump</a>";

                    webView.setWebViewClient(new WebViewClient());
                    webView.loadDataWithBaseURL(null, htmlcontent, "text/html", "utf-8", null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //WebSettings webSettings = webView.getSettings();
        //webSettings.setJavaScriptEnabled(true);
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
