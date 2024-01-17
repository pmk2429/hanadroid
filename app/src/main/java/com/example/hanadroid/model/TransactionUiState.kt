package com.example.hanadroid.model

/**
 * * The main benefit of using Interface here as opposed to a UiState data class object is to do with
 *  * how the properties and vars are populated. If we use Interface here, then the caller can define
 *  * the class level var in the class where these properties are updated only once and guarantee
 *  * that using a mix of local mutable variable and exposing backing property to that mutable variable,
 *  * we can pass these information to the caller.
 *  * This will particularly come in handy in primitive data classes such as Int, String etc.
 *  * Whereas, with data class, we can't just declare one single class member var and have it reference
 *  * to the backing fields because with data class, the class is created at run time and the exposed
 *  * backing field too. This makes the primitives such as Int, String to never change and hence the
 *  * changes won't be reflected in the data class.
 */
interface TransactionState {
    val universities: List<University>
    val total: Int
}

data class TransactionUiState(
    val university: List<University>,
    val total: Int
)

// Interface that implements the Listener
/**
 * interface TransactionListener {
 *
 *    fun onTransactionChange(state: TransactionState)
 *
 *    fun onTransactionUiStateChange(uiTransactionState: UiTransactionState)
 * }
 */

// Declaring the above two State objects as a class level member variable
/**
 * Inside of Activity or Fragment:
 *
 * private val transactionState = object : TransactionState {
 *         override val discounts: List<Discount>
 *             get() = discountsList
 *         override val items: List<Item>
 *             get() = itemsList
 *         override val total: Int
 *             get() = itemTotal
 *     }
 *
 * private val uiTransactionState = UiTransactionState(
 *         items = itemsList,
 *         discounts = discountsList,
 *         total = itemTotal
 * )
 */

// Now when we update the values in the callback listener:
/**
 * transactionListener?.onTransactionChange(state = transactionState)
 * transactionListener?.onTransactionUiStateChange(uiTransactionState = uiTransactionState.copy(total = _total)) ---> HERE
 *
 * Notice how the `uiTransactionState` is being referenced here.
 *
 * $ uiTransactionState = uiTransactionState.copy(total = _total)
 *
 * We can't directly call (uiTransactionState = uiTransactionState) since the object is declared
 * as a class level member variable, the value of primitive var total : Int will never change
 * We have to copy over the original uiTransactionState object and then update the total value to
 * pass down the value to the implementation in Activity or Fragment.
 */
