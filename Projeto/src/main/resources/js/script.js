// Simulando um banco de dados de produtos
const products = [
    {
        id: 1,
        name: "Nome do Produto 1",
        description: "Descrição detalhada do Produto 1.",
        price: "R$ 99,99",
        rating: "★★★★☆",
        mainImage: "garrafa1.jpg",
        thumbnails: ["garrafa1.jpg", "garrafa2.jpg", "garrafa3.jpg"]
    },
    {
        id: 2,
        name: "Nome do Produto 2",
        description: "Descrição detalhada do Produto 2.",
        price: "R$ 149,99",
        rating: "★★★★★",
        mainImage: "garrafa2.jpg",
        thumbnails: ["garrafa2.jpg", "garrafa1.jpg", "garrafa3.jpg"]
    },
    {
        id: 3,
        name: "Nome do Produto 3",
        description: "Descrição detalhada do Produto 3.",
        price: "R$ 79,99",
        rating: "★★★☆☆",
        mainImage: "garrafa3.jpg",
        thumbnails: ["garrafa3.jpg", "garrafa1.jpg", "garrafa2.jpg"]
    }
];

let cart = JSON.parse(localStorage.getItem('cart')) || [];

function addToCart(productName, productPrice) {
    const existingProductIndex = cart.findIndex(item => item.name === productName);
    if (existingProductIndex > -1) {
        cart[existingProductIndex].quantity += 1;
    } else {
        cart.push({ name: productName, price: productPrice, quantity: 1 });
    }
    localStorage.setItem('cart', JSON.stringify(cart));
    updateCartCount();
    alert('Produto adicionado ao carrinho!');
}

function updateCartCount() {
    document.getElementById('cart-count').textContent = cart.reduce((acc, item) => acc + item.quantity, 0);
}

document.querySelectorAll('.detail-button').forEach(button => {
    button.addEventListener('click', function(event) {
        event.preventDefault();
        const productId = this.getAttribute('data-product-id');
        displayProductDetail(productId);
    });
});

function displayProductDetail(productId) {
    const product = products.find(p => p.id == productId);
    if (product) {
        document.getElementById('modal-image').src = product.mainImage;
        document.getElementById('modal-product-name').textContent = product.name;
        document.getElementById('modal-product-description').textContent = product.description;
        document.getElementById('modal-product-price').textContent = product.price;
        document.getElementById('modal-product-rating').textContent = product.rating;

        const thumbnails = product.thumbnails;
        let currentIndex = 0;

        const updateImage = (index) => {
            document.getElementById('modal-image').src = thumbnails[index];
        };

        document.getElementById('prev').onclick = function() {
            currentIndex = (currentIndex === 0) ? thumbnails.length - 1 : currentIndex - 1;
            updateImage(currentIndex);
        };

        document.getElementById('next').onclick = function() {
            currentIndex = (currentIndex === thumbnails.length - 1) ? 0 : currentIndex + 1;
            updateImage(currentIndex);
        };

        const modal = document.getElementById('modal');
        modal.style.display = 'block'; // Abre a modal
    }
}

document.getElementById('close-modal').onclick = function() {
    const modal = document.getElementById('modal');
    modal.style.display = 'none'; // Fecha a modal
}

window.onclick = function(event) {
    const modal = document.getElementById('modal');
    if (event.target === modal) {
        modal.style.display = 'none'; // Fecha a modal ao clicar fora
    }
}

window.onload = function() {
    updateCartCount();
};
