package th.ac.kku.cis.final643450320_4

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import th.ac.kku.cis.final643450320_4.ui.theme.Final6434503204Theme
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Final6434503204Theme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "inputScreen") {
                    composable("inputScreen") {
                        InputScreen(navController)
                    }
                    composable("loginScreen") {
                        LoginScreen(navController)
                    }
                    composable("DisplayScreen/{name}/{password}") { backStackEntry ->
                        val arguments = requireNotNull(backStackEntry.arguments)
                        val name = arguments.getString("name") ?: ""
                        DisplayScreen(name = name, navController = navController)
                    }
                    composable("formScreen") {
                        FormScreen(navController)
                    }

                    composable("formScreen") {
                        FormScreen(navController)
                    }

                    composable("showDataScreen") {
                        ShowdataScreen(navController)
                    }
                }
            }
        }
    }
}

@Composable
fun InputScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.mooda01), // เพิ่มรูปภาพตามต้องการ
                contentDescription = null,
                modifier = Modifier.size(300.dp) // กำหนดขนาดของรูป

            )
            Text(text = "Register", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.padding(8.dp)
            )

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.padding(8.dp)
            )

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.padding(8.dp)
            )

            Button(
                onClick = {
                    saveDataToFirestore(name, email, password)
                    navController.navigate("loginScreen") // Navigate to the login screen
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Save")
            }
            Button(
                onClick = {
                    navController.navigate("loginScreen") // Navigate to the login screen
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Login")
            }
        }
    }
}

fun saveDataToFirestore(name: String, email: String, password: String) {
    val db = FirebaseFirestore.getInstance()
    val data = hashMapOf(
        "name" to name,
        "email" to email,
        "password" to password
    )
    db.collection("register") // Replace with your actual collection name
        .add(data)
        .addOnSuccessListener { documentReference ->
            Log.d("MainActivity", "DocumentSnapshot added with ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            Log.w("MainActivity", "Error adding document", e)
        }
}
@Composable
fun LoginScreen(navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("name") },
                modifier = Modifier.padding(8.dp)
            )

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.padding(8.dp)
            )

            Button(
                onClick = {
                    // Check if email and password match registered information
                    // For simplicity, direct navigation is done here, replace it with proper validation logic
                    navController.navigate("DisplayScreen/$name/$password")
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Login")
            }
        }
    }
}

@Composable
fun DisplayScreen(name: String, navController: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.mooda01), // เพิ่มรูปภาพตามต้องการ
                contentDescription = null,
                modifier = Modifier.size(400.dp) // กำหนดขนาดของรูป

            )
            Text("Welcome $name!", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("formScreen") }, // Wrap the navigation logic inside a lambda
                modifier = Modifier.padding(8.dp)
            ) {
                Text("How are you feeling? ")
            }
            Button(
                onClick = {
                    navController.navigate("showDataScreen")
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("diary")
            }
        }
    }
}

@Composable
fun FormScreen(navController: NavHostController) {
    var date by remember { mutableStateOf("") }
    var mood by remember { mutableStateOf("") }
    var diary by remember { mutableStateOf("") }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "how are you today?", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(50.dp))
            TextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date") },
                modifier = Modifier.padding(8.dp)
            )

            TextField(
                value = mood,
                onValueChange = { mood = it },
                label = { Text("Mood") },
                modifier = Modifier.padding(8.dp)
            )

            TextField(
                value = diary,
                onValueChange = {diary = it },
                label = { Text("write something.......") },
                modifier = Modifier.padding(8.dp)
                    .size(280.dp)

            )

            Button(
                onClick = {
                    saveMoodDataToFirestore(date, mood, diary)
                    navController.popBackStack()
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Save")
            }

            // Add a button to navigate back to the DisplayScreen
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    }
}

fun saveMoodDataToFirestore(date: String, mood: String, diary: String) {
    val db = FirebaseFirestore.getInstance()
    val data = hashMapOf(
        "Date" to date,
        "Mood" to mood,
        "Diary" to diary
    )
    db.collection("Mooda")
        .add(data)
        .addOnSuccessListener { documentReference ->
            Log.d("MainActivity", "DocumentSnapshot added with ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            Log.w("MainActivity", "Error adding document", e)
        }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowdataScreen(navController: NavHostController) {
    var dataList by remember { mutableStateOf<List<DocumentSnapshot>>(emptyList()) }
    var editingItemId by remember { mutableStateOf<String?>(null) }
    var editedDate by remember { mutableStateOf("") }
    var editedMood by remember { mutableStateOf("") }
    var editedDiary by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()
        val moodaRef = db.collection("Mooda")

        moodaRef.get()
            .addOnSuccessListener { documents ->
                dataList = documents.documents
            }
            .addOnFailureListener { exception ->
                Log.w("ShowdataScreen", "Error getting documents: ", exception)
            }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start // Align columns to the left
        ) {
            TopAppBar(
                title = { Text("Have a nice day!") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            dataList.forEach { document ->
                val date = document.getString("Date") ?: ""
                val mood = document.getString("Mood") ?: ""
                val diary = document.getString("Diary") ?: ""
                val documentId = document.id // เก็บ ID ของเอกสาร

                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth() // Make the card fill the available width
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        if (editingItemId == documentId) {
                            // Show editable fields
                            TextField(
                                value = editedDate,
                                onValueChange = { editedDate = it },
                                label = { Text("Edit Date") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = editedMood,
                                onValueChange = { editedMood = it },
                                label = { Text("Edit Mood") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = editedDiary,
                                onValueChange = { editedDiary = it },
                                label = { Text("Edit Diary") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        // Update Firestore with edited fields
                                        updateDocument(documentId, editedDate, editedMood, editedDiary)
                                        editingItemId = null // Stop editing mode
                                    },
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Text("Save")
                                }
                                Button(
                                    onClick = { editingItemId = null },
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Text("Cancel")
                                }
                            }
                        } else {
                            // Show non-editable text
                            Text(
                                text = "Date: $date\nMood: $mood\nDiary: $diary",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(
                                    onClick = { editingItemId = documentId; editedDate = date; editedMood = mood; editedDiary = diary },
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
                                }
                                IconButton(
                                    onClick = {
                                        // ลบข้อมูลใน Firebase Firestore
                                        deleteDocument(documentId)
                                    },
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

// ฟังก์ชันสำหรับอัปเดตข้อมูลใน Firebase Firestore โดยใช้ ID ของเอกสาร
private fun updateDocument(documentId: String, editedDate: String, editedMood: String, editedDiary: String) {
    val db = FirebaseFirestore.getInstance()
    val moodaRef = db.collection("Mooda").document(documentId)

    moodaRef.update(
        mapOf(
            "Date" to editedDate,
            "Mood" to editedMood,
            "Diary" to editedDiary
        )
    )
        .addOnSuccessListener {
            Log.d("ShowdataScreen", "DocumentSnapshot successfully updated!")
        }
        .addOnFailureListener { e ->
            Log.w("ShowdataScreen", "Error updating document", e)
        }
}


// ฟังก์ชันสำหรับลบข้อมูลใน Firebase Firestore โดยใช้ ID ของเอกสาร
private fun deleteDocument(documentId: String) {
    val db = FirebaseFirestore.getInstance()
    val moodaRef = db.collection("Mooda").document(documentId)

    moodaRef.delete()
        .addOnSuccessListener {
            Log.d("ShowdataScreen", "DocumentSnapshot successfully deleted!")
            // ทำอะไรก็ตามหลังจากลบข้อมูลสำเร็จ
        }
        .addOnFailureListener { e ->
            Log.w("ShowdataScreen", "Error deleting document", e)
        }
}