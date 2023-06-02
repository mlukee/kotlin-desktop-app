package data

import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import data.dao.mysql.*
import data.model.*
import data.ui.ItemRow

enum class MenuState { ITEMS, ABOUT_APP }

@Composable
fun Menu(menuState: MutableState<MenuState>, modifier: Modifier = Modifier) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Cyan)
            .composed { modifier },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.clickable { menuState.value = MenuState.ITEMS }.fillMaxWidth().weight(1f)
                .padding(15.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "data.Menu Icon",
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = "Items",
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.clickable { menuState.value = MenuState.ABOUT_APP }.fillMaxWidth().weight(1f)
                .padding(15.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Info Icon",
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = "About app",

                )
        }
    }


}

@Composable
@Preview
fun ItemsTab() {
    val itemsDB = MySqlItem().getAll()
    val internalItemsDB: List<InternalItem> = MySqlInternalItem().getAll()
    val allItems = itemsDB + internalItemsDB

    val state = rememberLazyListState()

    val items = remember { mutableStateListOf<Item>() }
    items.addAll(allItems)

    Box {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = state
        ) {
            items(items.size) { index ->
                val item = items[index]
                ItemRow(item, items)
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight().padding(start=5.dp),
            adapter = rememberScrollbarAdapter(scrollState = state)
        )
    }
}


@Composable
fun AboutAppTab() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "About application",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(10.dp),
            fontSize = 20.sp
        )
        Text(
            text = "Subject: Principles of programming languages",
            fontSize = 14.sp,
            modifier = Modifier.padding(5.dp)
        )
        Text(
            text = "Author: Matic Lukezic",
            fontSize = 14.sp,
        )
    }
}

@Composable
fun content(menuState: MutableState<MenuState>) {
    when (menuState.value) {
        MenuState.ITEMS -> ItemsTab()
        MenuState.ABOUT_APP -> AboutAppTab()
    }
}

@Composable
fun Footer(menuState: MutableState<MenuState>, modifier: Modifier = Modifier) {
    val activeMenu: String = when (menuState.value) {
        MenuState.ITEMS -> "Items"
        MenuState.ABOUT_APP -> "About app"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Cyan)
            .padding(8.dp)
            .then(modifier), // Apply the passed-in modifier
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "You're viewing the \"$activeMenu\" tab",
            fontSize = 14.sp
        )
    }
}

@Composable
@Preview
fun App() {
    val menuState = remember { mutableStateOf(MenuState.ABOUT_APP) }

    MaterialTheme {
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(bottom = 30.dp)
            ) {
                Menu(menuState)
                content(menuState)
            }
            Footer(menuState, Modifier.align(Alignment.BottomCenter))
        }
    }
}


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Vodenje racunov",
        state = rememberWindowState(
            position = WindowPosition.Aligned(Alignment.Center),
            size = DpSize(1200.dp, 1000.dp)
        ),
        undecorated = false,
        resizable = true
    ) {
        App()
    }

//    val kiwi = InternalItem("Kiwi", InternalID.KIWI, Department.SADJE, weight = 0.24)
//    val struckaZMakom = Item("Strucka z makom",0.89, TaxRate.RATE_9_5)
//    val napitekNescaffe = Item("Napitek nescafe", 1.45,TaxRate.RATE_9_5)
//    val kosmiciTus = Item("Kosmici TUŠ CHOC", 3.4, TaxRate.RATE_9_5)
//    val mehcalec = Item("MEHČ. Lenor",4.0, TaxRate.RATE_9_5)
//    val lubenica = InternalItem("Lubenica", InternalID.LUBENICA, Department.SADJE, weight = 1.5)
//    val kosilnica = Item("Kosilnica", 200.0, TaxRate.RATE_22)
//    val vilicar = Item("Vilicar", 100.0, TaxRate.RATE_22)
//    val banana = InternalItem("Banana", InternalID.BANANA, Department.SADJE, weight = 0.2)
//    val pepsi = Item("Pepsi", 1.0, TaxRate.RATE_9_5)
//    val zala = Item("Zala", 0.99, TaxRate.RATE_9_5)
//    val invoiceDao = MySqlInvoice()
//    val itemDao = MySqlItem()
//    val internalItemDao = MySqlInternalItem()
//    val itemsDao = MySqlItems()
//    val internalItemsDao = MySqlInternalItems()
//    val invoice = invoiceDao.getAll().first()
//    itemDao.insert(struckaZMakom)
//    itemDao.insert(napitekNescaffe)
//    itemDao.insert(kosmiciTus)
//    itemDao.insert(mehcalec)
//    itemDao.insert(kosilnica)
//    itemDao.insert(vilicar)
//    internalItemDao.insert(banana)
//    itemDao.insert(pepsi)
//    itemDao.insert(zala)
//    internalItemDao.insert(kiwi)
//    internalItemDao.insert(lubenica)
//    itemsDao.insert(invoice.id, struckaZMakom.id, 2)
//    itemsDao.insert(invoice.id, napitekNescaffe.id, 1)
//    itemsDao.insert(invoice.id, kosmiciTus.id, 1)
//    itemsDao.insert(invoice.id, mehcalec.id, 1)
//    itemsDao.insert(invoice.id, kosilnica.id, 1)
//    itemsDao.insert(invoice.id, vilicar.id, 1)
//    itemsDao.insert(invoice.id, pepsi.id, 1)
//    itemsDao.insert(invoice.id, zala.id, 1)
//    internalItemsDao.insert(invoice.id, banana.id)
//    internalItemsDao.insert(invoice.id, kiwi.id)
//    internalItemsDao.insert(invoice.id, lubenica.id)
}