# Manus till SameGame-redovisning, cirka 10 minuter

## 1. Inledning, cirka 1 minut

Hej, vi ska presentera vårt projekt SameGame, som är ett objektorienterat pusselspel skrivet i Java.

SameGame är ett tile-baserat spel där spelaren tar bort grupper av brickor med samma färg. En grupp räknas som brickor som sitter ihop horisontellt eller vertikalt. När en grupp tas bort faller brickorna ovanför ner, och tomma kolumner flyttas åt vänster. Spelet fortsätter tills spelaren antingen har rensat hela brädet, eller tills det inte finns några giltiga drag kvar.

Målet med projektet var inte att göra avancerad grafik eller animationer, utan att visa en bra objektorienterad design. Därför har vi fokuserat på ansvarsfördelning, MVC, Observer pattern, testbarhet och att det ska vara enkelt att bygga ut spelet.

## 2. Projektkrav och mål, cirka 1 minut

De viktigaste kraven var att skapa ett fungerande SameGame i Java, att använda en tile-baserad modell och att visa spelet genom minst två olika vyer.

Vi har därför implementerat både en grafisk vy med Swing och en terminalvy. Båda visar samma spelstate och uppdateras automatiskt när modellen ändras.

Ett annat krav var att använda designprinciper från kursen. Därför har vi strukturerat projektet enligt MVC:

- `GameModel` är modellen.
- `GuiView` och `TerminalView` är vyerna.
- `GameController` är controllern.

Utöver detta använder vi Observer pattern för att hålla vyerna synkroniserade, och ett separat observer-system för ljudeffekter.

## 3. MVC-arkitektur, cirka 2 minuter

Den centrala designen i projektet är MVC.

Modellen, alltså `GameModel`, äger själva spelstate. Den håller koll på brädet, nuvarande poäng, bästa poäng, svårighetsgrad och om spelet är i läget `PLAYING`, `WON` eller `LOST`.

Själva brädet representeras av klassen `Board`, som innehåller en tvådimensionell array av `Tile`. Varje `Tile` har en färg från enumen `Color`. `Board` ansvarar för grundläggande operationer som att hämta och sätta en bricka, kontrollera om en position är giltig och skapa ett randomiserat bräde utifrån vald svårighetsgrad.

Controllern, `GameController`, är mellanlagret mellan input och modell. Om spelaren klickar i GUI:t eller skriver en rad och kolumn i terminalen, går det via controllern. Controllern validerar först att spelet fortfarande pågår, att positionen finns på brädet, att det finns en bricka där, och att gruppen har minst två brickor. Om draget är giltigt anropar den modellen för att ta bort gruppen.

Vyerna har ansvar för presentation och input. `GuiView` visar brädet grafiskt med Swing och hanterar musklick. `TerminalView` skriver ut brädet som text och läser kommandon från användaren. Båda vyerna läser från modellen, men själva spelreglerna ligger inte i vyerna.

Det här gör att ansvaren blir tydliga. Om vi ändrar reglerna i spelet behöver vi främst ändra i modellen. Om vi ändrar hur spelet visas behöver vi ändra i vyerna. Om vi lägger till en ny typ av input kan vi använda samma controller.

## 4. Observer pattern, cirka 1.5 minuter

För att hålla flera vyer synkroniserade använder vi Observer pattern.

`GameModel` fungerar som subject. Den har en lista med `GameObserver`. Både `GuiView` och `TerminalView` implementerar `GameObserver`, vilket betyder att de måste ha metoden `updateBoard(Board board)`.

När något ändras i modellen, till exempel när spelaren tar bort en grupp brickor, kör modellen:

1. tar bort brickorna,
2. kollapsar brädet vertikalt,
3. kollapsar brädet horisontellt,
4. uppdaterar poäng och game state,
5. notifierar observerarna.

Efter det får både GUI:t och terminalen samma uppdaterade bräde. Det gör att vyerna inte behöver fråga modellen hela tiden. De blir uppdaterade när något faktiskt har ändrats.

Vi har också ett separat observer-system för ljud. Där finns `SoundObserver`, och ljudklasserna `TileClearSound`, `WinSound` och `LostSound` lyssnar på olika `SoundEvent`. På så sätt är ljud inte hårdkodat i GUI:t eller terminalen. Modellen skickar bara en händelse, till exempel `TILE_CLEAR`, `WON` eller `LOST`, och rätt ljudklass reagerar.

Det är ett exempel på loose coupling. Modellen behöver inte veta exakt hur ljudet spelas upp, bara att det finns observatörer som kan hantera ljudhändelsen.

## 5. Spelregler och algoritmer, cirka 2 minuter

Den mest intressanta algoritmen är hur vi hittar sammanhängande grupper av brickor. Det görs i `GameModel` med en flood-fill-liknande algoritm.

När spelaren väljer en bricka letar modellen efter alla brickor med samma färg som sitter ihop horisontellt eller vertikalt. Den använder en kö med positioner som ska kontrolleras och en `visited`-matris så att samma position inte behandlas flera gånger.

Det här gör att vi kan hitta hela gruppen oavsett form. Gruppen kan vara rak, L-formad eller mer oregelbunden.

När en grupp har hittats och är giltig, alltså minst två brickor, tas brickorna bort genom att deras positioner sätts till `null`.

Efter det sker två kollapser:

Först kollapsar brädet vertikalt. Varje kolumn gås igenom nerifrån och upp, och alla kvarvarande brickor packas mot botten. Det gör att tomma rutor hamnar längst upp.

Sedan kollapsar brädet horisontellt. Om en hel kolumn är tom flyttas alla kolumner till höger om den åt vänster. Det motsvarar reglerna i SameGame.

Poängen beräknas med formeln:

```
(tilesRemoved - 2) * (tilesRemoved - 2)
```

Det betyder att små grupper ger lite eller ingen poäng, medan stora grupper ger mycket mer. Det uppmuntrar spelaren att planera och skapa större grupper.

Vi har också lagt till en move suggestion-funktion. `getBestMoveSuggestion()` går igenom brädet, hittar grupper och föreslår den största gruppen just nu. Den visas både i GUI:t och terminalen.

## 6. High score, svårighetsgrad och extra funktioner, cirka 1 minut

Spelet har flera svårighetsgrader genom att man kan välja mellan 2 och 5 färger. Färre färger gör det lättare att skapa stora grupper, medan fler färger gör spelet svårare.

Vi har också en `HighScoreManager` som sparar high scores i filen `highscores.txt`. När spelet tar slut läggs poängen till i high score-listan, listan sorteras och sparas till fil.

Det gör att bästa poängen finns kvar även när programmet startas om.

I GUI:t visas current score, best score, difficulty level och en knapp för att få bästa dragförslag. När spelet är över visas också en status, till exempel att det inte finns fler drag, och man kan starta ett nytt spel med ny svårighetsgrad.

## 7. Tester och versionshantering, cirka 1 minut

Vi har skrivit JUnit-tester för de viktigaste delarna av spelet.

I `BoardTest` testar vi att positioner på brädet valideras korrekt, att brickor kan sättas och hämtas, och att svårighetsgraden bara accepterar värden mellan 2 och 5.

I `GameModelTest` testar vi bland annat att flood-fill hittar rätt sammanhängande brickor, att brädet kollapsar neråt och åt vänster efter borttagning, och att move suggestion hittar den största gruppen.

I `GameControllerTest` testar vi att giltiga drag tas bort, att ogiltiga drag nekas, och att nytt spel kan startas med vald svårighetsgrad.

Vi har också använt Git och GitHub under arbetet, vilket gjorde det lättare att samarbeta och hålla reda på ändringar.

## 8. Designstyrkor och möjliga förbättringar, cirka 1 minut

En styrka i designen är att ansvaren är separerade. Modellen innehåller reglerna, controllern tolkar drag, och vyerna visar spelet. Det gör projektet lättare att förstå och bygga vidare på.

Observer pattern gör också att vi kan lägga till fler vyer eller observers utan att skriva om modellen mycket. Till exempel skulle man kunna lägga till en replay view, en loggvy eller en nätverksbaserad klient.

En möjlig förbättring är att göra input-systemet ännu mer pluggable, till exempel genom ett tydligare strategy pattern för olika inputmetoder. Just nu finns både terminalinput och musinput, men de är implementerade direkt i respektive vy.

En annan förbättring är att bryta ut ljuduppspelningen lite mer, eftersom `TileClearSound`, `WinSound` och `LostSound` har liknande kod för att läsa och spela upp ljudfiler.

Man skulle också kunna förbättra GUI:t med bättre layout, animationer eller tydligare information om high score-listan, men det var inte huvudfokus i projektet.

## 9. Avslutning, cirka 30 sekunder

Sammanfattningsvis har vi byggt ett fungerande SameGame i Java med två synkroniserade vyer, MVC-struktur, Observer pattern, ljudhändelser, high score, svårighetsgrad, move suggestions och tester.

Projektet visar hur objektorienterad design kan användas för att hålla spelregler, input och presentation separerade. Det gör koden mer underhållbar och gör det lättare att lägga till nya funktioner i framtiden.

Det var vår redovisning av SameGame.

