package data.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.dao.mysql.MySqlInternalItem
import data.dao.mysql.MySqlInternalItems
import data.dao.mysql.MySqlItem
import data.dao.mysql.MySqlItems
import data.model.InternalItem
import data.model.Item

fun handleDeleteItemClick(item: Item, items: MutableList<Item>) {
    val mySqlItem = MySqlItem()
    val mySqlItems = MySqlItems()
    var deleted = mySqlItems.delete(item.id)
    if (deleted) {
        deleted = mySqlItem.delete(item)
        if(deleted) {
            items.removeIf { it.id == item.id }
        }
    }else{
        val mySqlInternalItems = MySqlInternalItems()
        val mySqlInternalItem = MySqlInternalItem()
        deleted = mySqlInternalItems.delete(item.id)
        if(deleted){
            deleted = mySqlInternalItem.delete(item as InternalItem)
            if(deleted){
                items.removeIf { it.id == item.id }
            }
        }
    }

    println("Delete icon clicked for item: ${item.id}")
}

fun handleEditItemClick(item: Item) {
    val mySqlItem = MySqlItem()
    val mySqlInternalItem = MySqlInternalItem()
    var updated = mySqlItem.update(item)
    if(!updated){
        mySqlInternalItem.update(item as InternalItem)
    }
    println("Edit icon clicked for item: ${item.id}")
}

@Composable
@Preview
fun ItemRow(item: Item, items: MutableList<Item>) {
    var editing by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf(item.name) }
    var editedPrice by remember { mutableStateOf(item.price) }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(
                BorderStroke(0.dp, Color.Black),
                shape = RoundedCornerShape(10.dp)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Shopping Cart Icon",
                modifier = Modifier.padding(end = 4.dp).height(80.dp).width(80.dp)
            )
            if (editing) {
                OutlinedTextField(
                    value=  editedName,
                    onValueChange = { editedName = it },
                    label = { Text(text = "Name") },
                    modifier = Modifier.padding(5.dp)
                )
                OutlinedTextField(
                    value=  editedPrice.toString(),
                    onValueChange = { editedPrice = it.toDouble() },
                    label = { Text(text = "Price") },
                )


            } else {
                Text(
                    text = item.name,
                    modifier = Modifier.padding(5.dp),
                    fontSize = 20.sp
                )
            }
        }
        Row {
            IconButton(
                onClick = {
                    if (editing) {
                        item.name = editedName
                        item.price = editedPrice
                        handleEditItemClick(item)
                    }
                    editing = !editing
                }
            ) {
                Icon(
                    imageVector = if (editing) Icons.Default.Done else Icons.Default.Edit,
                    modifier = Modifier.height(20.dp).width(20.dp),
                    contentDescription = if (editing) "Done Icon" else "Edit Icon"
                )
            }
            IconButton(
                onClick = {
                    if(editing){
                        editing = !editing
                    }else{
                        handleDeleteItemClick(item, items) }
                    }
            ) {
                Icon(
                    imageVector = if(editing) Icons.Default.Close else Icons.Default.Delete,
                    modifier = Modifier.height(20.dp).width(20.dp),
                    contentDescription = "Delete Icon"
                )
            }
        }
    }
}
