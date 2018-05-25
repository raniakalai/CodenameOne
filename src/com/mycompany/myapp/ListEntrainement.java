/*
 * Copyright (c) 2016, Codename One
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, 
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions 
 * of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A 
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 
 */

package com.mycompany.myapp;

import ca.weblite.codename1.json.JSONArray;
import ca.weblite.codename1.json.JSONException;
import ca.weblite.codename1.json.JSONObject;
import com.codename1.charts.util.ColorUtil;
import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.io.rest.Response;
import com.codename1.io.rest.Rest;
import com.codename1.ui.Button;
import com.codename1.ui.ButtonGroup;
import static com.codename1.ui.CN.callSerially;
import com.codename1.ui.Component;
import static com.codename1.ui.Component.BOTTOM;
import static com.codename1.ui.Component.CENTER;
import static com.codename1.ui.Component.LEFT;
import static com.codename1.ui.Component.RIGHT;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.List;
import com.codename1.ui.RadioButton;
import com.codename1.ui.Tabs;
import com.codename1.ui.TextArea;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.util.Resources;
import com.mycompany.entities.Monitor;
import com.mycompany.entities.Voiture;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * The newsfeed form
 *
 * @author Shai Almog
 */
public class ListEntrainement extends BaseForm {
    int idvoiture=0,idmoniteur=0;
    
    public ListEntrainement(Resources res) {
        super("Newsfeed", BoxLayout.y());
        Toolbar tb = new Toolbar(true);
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle("Entrainement");
        getContentPane().setScrollVisible(false);
        
        super.addSideMenu(res);
        //tb.addSearchCommand(e -> {});
        
        Tabs swipe = new Tabs();

        Label spacer1 = new Label();
        Label spacer2 = new Label();
        addTab(swipe, res.getImage("autoecole.jpg"), spacer1, "15 Likes  ", "85 Comments", "Integer ut placerat purued non dignissim neque. ");
        //addTab(swipe, res.getImage("dog.jpg"), spacer2, "100 Likes  ", "66 Comments", "Dogs are cute: story at 11");
                
        swipe.setUIID("Container");
        swipe.getContentPane().setUIID("Container");
        swipe.hideTabs();
        
        ButtonGroup bg = new ButtonGroup();
        int size = Display.getInstance().convertToPixels(1);
        Image unselectedWalkthru = Image.createImage(size, size, 0);
        Graphics g = unselectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAlpha(100);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        Image selectedWalkthru = Image.createImage(size, size, 0);
        g = selectedWalkthru.getGraphics();
        g.setColor(0xffffff);
        g.setAntiAliased(true);
        g.fillArc(0, 0, size, size, 0, 360);
        RadioButton[] rbs = new RadioButton[swipe.getTabCount()];
        FlowLayout flow = new FlowLayout(CENTER);
        flow.setValign(BOTTOM);
        Container radioContainer = new Container(flow);
        for(int iter = 0 ; iter < rbs.length ; iter++) {
            rbs[iter] = RadioButton.createToggle(unselectedWalkthru, bg);
            rbs[iter].setPressedIcon(selectedWalkthru);
            rbs[iter].setUIID("Label");
            radioContainer.add(rbs[iter]);
        }
                
//        rbs[0].setSelected(true);
//        swipe.addSelectionListener((i, ii) -> {
//            if(!rbs[ii].isSelected()) {
//                rbs[ii].setSelected(true);
//            }
//        });
       
        Component.setSameSize(radioContainer, spacer1, spacer2);
        add(LayeredLayout.encloseIn(swipe, radioContainer));
        
        ButtonGroup barGroup = new ButtonGroup();
        RadioButton all = RadioButton.createToggle("Tout", barGroup);
        all.setUIID("SelectBar");
        
        //Label arrow = new Label(res.getImage("news-tab-down-arrow.png"), "Container");
        
        System.out.println("All Name = "+all.getText());
        
        add(LayeredLayout.encloseIn(
                GridLayout.encloseIn(1, all)
        ));
        setScrollableY(true);
        Container list = new Container(BoxLayout.y());
        list.setScrollableY(true);
        add(list);
       
        
        
        // special case for rotation
       
        
        ListEntrainements(res,list);

    }
    
    private void ListEntrainements(Resources res, Container l){
      
        l.removeAll();
        l.setScrollableY(true);
        Response<String> resultData = Rest.get("http://localhost/pirania/web/app_dev.php/ws/entrainement").acceptJson().getAsString();
            if(resultData.getResponseCode() != 200) {
                callSerially(() -> {
                    ToastBar.showErrorMessage("Erreur\n");
                });
            }
            JSONArray index;
        try {
            index = new JSONArray(resultData.getResponseData());
            //System.out.println(index);
            for ( int i=0; i<index.length(); i++){
                JSONObject moniteur = index.getJSONObject(i);
                if (moniteur.getInt("candidate_id")==SignIn.candidat.getId()){
                    JSONObject date = moniteur.getJSONObject("dateentrainement");
                    Voiture v = getVoiture(moniteur.getInt("voiture_id"));
                    Monitor m = getMonitor(moniteur.getInt("moniteur_id"));
                    addButton(res.getImage("news-item-1.jpg"), date.getString("date").substring(0, date.getString("date").length()-7),m, v,  l);
                }
            }
        } catch (JSONException ex) {
            System.out.println(ex.getMessage());
        }  
    }
    
   Voiture getVoiture (int id ){
       
       Voiture v = new Voiture();
       Response<String> resultData = Rest.get("http://localhost/pirania/web/app_dev.php/ws/voiture/"+id).acceptJson().getAsString();
            if(resultData.getResponseCode() != 200) {
                callSerially(() -> {
                    ToastBar.showErrorMessage("Erreur\n");
                });
            }
            JSONArray index;
        try {
            index = new JSONArray(resultData.getResponseData());
            JSONObject i = index.getJSONObject(0);
                v.setAge(i.getInt("age"));
                v.setCouleur(i.getString("couleur"));
                v.setCout(i.getInt("cout"));
                v.setId(i.getInt("id"));
                v.setImmatricule(i.getString("immatricule"));
                v.setMarque(i.getString("marque"));
                v.setModele(i.getString("modele"));
        } catch (JSONException ex) {
            System.out.println(ex.getMessage());
        } 
        return v;
   }
    
    Monitor getMonitor (int id ){
       
       Monitor v = new Monitor();
       Response<String> resultData = Rest.get("http://localhost/pirania/web/app_dev.php/ws/monitor/"+id).acceptJson().getAsString();
            if(resultData.getResponseCode() != 200) {
                callSerially(() -> {
                    ToastBar.showErrorMessage("Erreur\n");
                });
            }
            JSONArray index;
        try {
            index = new JSONArray(resultData.getResponseData());
            JSONObject i = index.getJSONObject(0);
                v.setPrenom(i.getString("prenom"));
                v.setNom(i.getString("nom"));
                v.setId(i.getInt("id"));
                
        } catch (JSONException ex) {
            System.out.println(ex.getMessage());
        } 
        return v;
   }
    
    private void updateArrowPosition(Button b, Label arrow) {
        arrow.getUnselectedStyle().setMargin(LEFT, b.getX() + b.getWidth() / 2 - arrow.getWidth() / 2);
        arrow.getParent().repaint();
    }
    
    private void addTab(Tabs swipe, Image img, Label spacer, String likesStr, String commentsStr, String text) {
        int size = Math.min(Display.getInstance().getDisplayWidth(), Display.getInstance().getDisplayHeight());
        if(img.getHeight() < size) {
            img = img.scaledHeight(size);
        }
        Label likes = new Label(likesStr);
        Style heartStyle = new Style(likes.getUnselectedStyle());
        heartStyle.setFgColor(0xff2d55);
        FontImage heartImage = FontImage.createMaterial(FontImage.MATERIAL_FAVORITE, heartStyle);
        likes.setIcon(heartImage);
        likes.setTextPosition(RIGHT);

        Label comments = new Label(commentsStr);
        FontImage.setMaterialIcon(comments, FontImage.MATERIAL_CHAT);
        if(img.getHeight() > Display.getInstance().getDisplayHeight() / 2) {
            img = img.scaledHeight(Display.getInstance().getDisplayHeight() / 2);
        }
        ScaleImageLabel image = new ScaleImageLabel(img);
        image.setUIID("Container");
        image.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);
        Label overlay = new Label(" ", "ImageOverlay");
        Button entrainementbutton = new Button("Ajouter E");
        
        
                    entrainementbutton.addActionListener((ActionListener) (ActionEvent evt) -> {
                        ToastBar.showMessage("Button Clicked", FontImage.MATERIAL_INFO);
                    });
                    Container page1 = 
                        LayeredLayout.encloseIn(
                            image,
                            overlay
                       
                        );

        swipe.addTab("", page1);
    }
    
   private void addButton(Image img, String title, Monitor mon, Voiture voit , Container l) {
       int height = Display.getInstance().convertToPixels(11.5f);
       int width = Display.getInstance().convertToPixels(14f);
       Button image = new Button(img.fill(width, height));
       image.setUIID("Label");
       Container cnt = BorderLayout.west(image);
      
       cnt.getAllStyles().setBgColor(ColorUtil.MAGENTA );
       cnt.setLeadComponent(image);
       TextArea ta = new TextArea(title);
       ta.setUIID("NewsTopLine");
       ta.setEditable(false);

       Label likes = new Label(voit.getMarque()+" "+voit.getModele(), "NewsBottomLine");
       likes.setTextPosition(RIGHT);
       
       Label comments = new Label("Mr/Mm "+mon.getNom()+" "+mon.getPrenom(), "NewsBottomLine");
       //FontImage.setMaterialIcon(likes, FontImage.MATERIAL_CHAT);
       
       
       cnt.add(BorderLayout.CENTER, 
               BoxLayout.encloseY(
                       ta,
                       BoxLayout.encloseX(likes, comments)
               ));
       l.add(cnt);
       //image.addActionListener(e -> ToastBar.showMessage(title, FontImage.MATERIAL_INFO));
   }
   
   
   
    
    
    
    void ajouter(Container l){
        l.removeAll();
        Picker dateTimePicker = new Picker();
        dateTimePicker.setType(Display.PICKER_TYPE_DATE_AND_TIME);
        //dateTimePicker.setText("Date d'entarinement");
        dateTimePicker.setDate(new Date());
        
                       Button entrainementbutton = new Button("Ajouter Entrainement");
        
        
                    entrainementbutton.addActionListener((ActionListener) (ActionEvent evt) -> {
                        Date d = new Date();
                        if ((int)(d.getTime()-dateTimePicker.getDate().getTime())<0){
                            if (idvoiture == 0 || idmoniteur ==0) {
                                ToastBar.showMessage("you need to choose a car and monitor first", FontImage.MATERIAL_INFO);

                            }else{
                                System.out.println("http://localhost/pirania/web/app_dev.php/ws/entrainement/create/"+SignIn.candidat.getId()+"/"+idmoniteur+"/"+idvoiture+"/"+dateTimePicker.getDate().getTime());
                                Response<String> resultData = Rest.get("http://localhost/pirania/web/app_dev.php/ws/entrainement/create/"+SignIn.candidat.getId()+"/"+idmoniteur+"/"+idvoiture+"/"+dateTimePicker.getDate()).acceptJson().getAsString();
                                if(resultData.getResponseCode() != 200) {
                                    callSerially(() -> {
                                        ToastBar.showErrorMessage("Erreur\n");
                                    });
                                }
                            
                                ToastBar.showMessage("Added", FontImage.MATERIAL_INFO);
                            }
                        }else{
                            ToastBar.showMessage("Invalide Date", FontImage.MATERIAL_INFO);
                        }
                    });
                    Container page1 = 
                        LayeredLayout.encloseIn(
            //                image,
            //                overlay,
                                
                            BorderLayout.south(
                                BoxLayout.encloseY(
                                        dateTimePicker,
                                        entrainementbutton
                                       )
                            )
                        );
                    l.add(page1);
                    l.refreshTheme();
    }
}
