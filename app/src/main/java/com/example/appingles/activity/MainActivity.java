package com.example.appingles.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.appingles.R;
import com.example.appingles.config.ConfiguracaoFirebase;
import com.example.appingles.fragments.PerfilFragment;
import com.example.appingles.fragments.FrasesDoDiaFragment;
import com.example.appingles.fragments.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth usuarioAutenticacao;
    private BottomNavigationView navigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.nav_frasesdodia:
                    infla(new FrasesDoDiaFragment(), "FrasesDia");
                    return true;
                case R.id.nav_home:
                    infla(new HomeFragment(), "Home");
                    return true;
                case R.id.nav_favoritos:
                    infla(new PerfilFragment(), "Favoritos");
                    return true;
            }
            return false;
        }
    };

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.bottom_NavigationView);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        infla(new HomeFragment(), "Home");
        usuarioAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        toolbar = findViewById(R.id.toolbar);
        //toolbar.setTitle("Teste");
        setSupportActionBar( toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.item_sair:
                deslogarUsuario();
                return true;
            case R.id.item_pesquisa:
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }

    }

    public void deslogarUsuario(){
        usuarioAutenticacao.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void infla(Fragment fragmento, String tag){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmento, tag).commit();
    }
}
