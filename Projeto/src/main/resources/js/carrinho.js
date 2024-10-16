let cart = JSON.parse(localStorage.getItem('cart')) || [];

// Função para exibir os itens do carrinho
function displayCart() {
    const cartItemsContainer = document.getElementById('cart-items');
    const totalPriceContainer = document.getElementById('total-price');

    // Limpa os itens do carrinho exibidos
    cartItemsContainer.innerHTML = '';

    // Verifica se o carrinho está vazio
    if (cart.length === 0) {
        cartItemsContainer.innerHTML = '<p>O carrinho está vazio.</p>';
        totalPriceContainer.textContent = '0.00';
        return;
    }

    let total = 0;

    // Loop pelos itens do carrinho
    cart.forEach(item => {
        const itemDiv = document.createElement('div');
        itemDiv.classList.add('cart-item');
        itemDiv.innerHTML = `
            <h3>${item.name}</h3>
            <p>Preço: ${item.price}</p>
            <p>Quantidade: <span class="quantity">${item.quantity}</span></p>
            <button onclick="increaseQuantity('${item.name}')">+</button>
            <button onclick="decreaseQuantity('${item.name}')">-</button>
        `;
        cartItemsContainer.appendChild(itemDiv);

        // Adiciona o preço ao total
        const priceValue = parseFloat(item.price.replace('R$ ', '').replace(',', '.'));
        total += priceValue * item.quantity; // Corrige o cálculo do total
    });

    totalPriceContainer.textContent = total.toFixed(2); // Atualiza o total na tela
}

// Função para aumentar a quantidade de um item no carrinho
function increaseQuantity(productName) {
    const product = cart.find(item => item.name === productName);
    if (product) {
        product.quantity += 1;
        localStorage.setItem('cart', JSON.stringify(cart)); // Atualiza o localStorage
        displayCart(); // Atualiza a exibição do carrinho
    }
}

// Função para diminuir a quantidade de um item no carrinho
function decreaseQuantity(productName) {
    const product = cart.find(item => item.name === productName);
    if (product) {
        product.quantity -= 1;
        if (product.quantity <= 0) {
            cart = cart.filter(item => item.name !== productName); // Remove o item se a quantidade for 0
        }
        localStorage.setItem('cart', JSON.stringify(cart)); // Atualiza o localStorage
        displayCart(); // Atualiza a exibição do carrinho
    }
}

// Chama a função de exibição do carrinho quando a página de carrinho é carregada
window.onload = function() {
    displayCart();
    updateCartCount();
};

// Atualiza a contagem total de produtos no carrinho
function updateCartCount() {
    document.getElementById('cart-count').textContent = cart.reduce((acc, item) => acc + item.quantity, 0);
}


