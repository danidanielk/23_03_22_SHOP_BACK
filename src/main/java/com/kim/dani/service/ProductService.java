package com.kim.dani.service;

import com.kim.dani.dtoSet.*;
import com.kim.dani.entity.*;
import com.kim.dani.jwt.JwtTokenV2;
import com.kim.dani.repository.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {



    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartAndProductRepository cartAndProductRepository;
    private final CartProductRepository cartProductRepository;
    private final RecentlyProductRepository recentlyProductRepository;
    private final BookmarkRepository bookmarkRepository;
    private final JPAQueryFactory queryFactory;
    private final QRecentlyProduct qRecentlyProduct = QRecentlyProduct.recentlyProduct;
    private final QBookmark qBookmark = QBookmark.bookmark;
    private final QProduct qProduct = QProduct.product;
    private final QMember qMember = QMember.member;
    private final QCart qCart = QCart.cart;
    private final QCartProduct qCartProduct = QCartProduct.cartProduct;
    private final QCartAndProduct qCartAndProduct = QCartAndProduct.cartAndProduct;
    private final JwtTokenV2 jwtTokenV2;






    //카테고리에 맞는 상품 리스트
    public List<ProductListSetDto> productList(String category){

        List<ProductListSetDto> setDtoList = new ArrayList<>();
        List<Product> products = queryFactory
                .selectFrom(qProduct)
                .where(qProduct.category.productCategory.eq(category))
                .fetch();

        for (Product product : products) {
        ProductListSetDto setDto = new ProductListSetDto(product.getId(),
                product.getProductName(), product.getProductImage(),
                product.getProductPrice(), product.getProductContent(),
                product.getProductQuantity(), product.getCategory().getProductCategory());
            setDtoList.add(setDto);
        }
        return setDtoList;
    }



    //상품 디테일
    public ProductDetailSetDto productDetail(Long productId,HttpServletRequest req,HttpServletResponse res){

        String getEmail = jwtTokenV2.tokenValidatiorAndGetEmail(req, res);

        Member member = queryFactory
                .selectFrom(qMember)
                .where(qMember.email.eq(getEmail))
                .fetchOne();


        Product product = queryFactory
                .selectFrom(qProduct)
                .where(qProduct.id.eq(productId))
                .fetchOne();


        //최근본상품 DB에 저장
        RecentlyProduct recentlyProduct = new RecentlyProduct();
        recentlyProduct.setMember(member);
        recentlyProduct.setProduct(product);
        recentlyProductRepository.save(recentlyProduct);



        return new ProductDetailSetDto(product.getId(), product.getProductName(),
                product.getProductImage(), product.getProductPrice(),
                product.getProductContent(), product.getProductQuantity(),
                product.getCategory().getProductCategory());




    }

    //상품 ADD
    public boolean productAdd(Long productId ,Long inQuantity,Long setPrice, HttpServletRequest req, HttpServletResponse res) {
        String MemberEmail = jwtTokenV2.tokenValidatiorAndGetEmail(req,res);

        Product product = queryFactory
                .selectFrom(qProduct)
                .where(qProduct.id.eq(productId))
                .fetchOne();

        Member member = queryFactory
                .selectFrom(qMember)
                .where(qMember.email.eq(MemberEmail))
                .fetchOne();

        CartProduct cartProduct = new CartProduct(null,product.getId(), product.getProductName(), product.getProductImage(),
                String.valueOf(setPrice), product.getProductContent(), inQuantity,product.getCategory().getProductCategory(),member,null);

        cartProductRepository.save(cartProduct);

        Cart cart = queryFactory
                .selectFrom(qCart)
                .where(qCart.member.eq(member))
                .fetchOne();

        List<CartProduct> products = queryFactory
                .select(qCartProduct)
                .from(qMember)
                .leftJoin(qMember.cart, qCart)
                .leftJoin(qCart.cartAndProduct, qCartAndProduct)
                .leftJoin(qCartAndProduct.cartProduct, qCartProduct)
                .where(qMember.eq(member))
                .fetch();


        if (cart == null){
            Cart cart1 = new Cart(null, member, null);
            cartRepository.save(cart1);
            CartAndProduct cartAndProduct = new CartAndProduct(null, cart1, cartProduct);
            cartAndProductRepository.save(cartAndProduct);
            return true;
        }

//        for (Product product1 : products) {
//            if (product1.getId().equals(productId)) {
//                return false;
//            }
//        }

        CartAndProduct cartAndProduct = new CartAndProduct(null, cart, cartProduct);
        cartAndProductRepository.save(cartAndProduct);
        return true;

    }


    //전체상품 리스트
    public List<ProductListAllSetDto> listAll(){
        List<Product> products = queryFactory
                .selectFrom(qProduct)
                .fetch();

        List<ProductListAllSetDto> productListAllSetDtos = new ArrayList<>();

        for (Product product : products) {
            ProductListAllSetDto productListAllSetDto = new ProductListAllSetDto(product.getId(), product.getProductName(),
                    product.getProductImage(), product.getProductPrice(), product.getProductPrice(), product.getProductQuantity(),
                    product.getCategory().getProductCategory());
            productListAllSetDtos.add(productListAllSetDto);
        }
        return productListAllSetDtos;
    }


    //즐겨찾기에 추가
    public boolean getBookmark(Long productId,HttpServletRequest req, HttpServletResponse res) {

        String getEmail = jwtTokenV2.tokenValidatiorAndGetEmail(req,res);

        if (getEmail != null) {

        Product product  = queryFactory
                .selectFrom(qProduct)
                .where(qProduct.id.eq(productId))
                .fetchOne();

        Member member = queryFactory
                .selectFrom(qMember)
                .where(qMember.email.eq(getEmail))
                .fetchOne();

        Bookmark bookmark = new Bookmark();
        bookmark.setMember(member);
        bookmark.setProduct(product);
        bookmarkRepository.save(bookmark);
            return true;
        }
        return false;
    }


    //즐겨찾기, 최근본상품 목록
    public RecentlyAndBookmarkSetDto myProduct(HttpServletRequest req, HttpServletResponse res) {

        String getEmail = jwtTokenV2.tokenValidatiorAndGetEmail(req, res);

        List<RecentlyProduct> recentlyProducts = queryFactory
                .selectFrom(qRecentlyProduct)
                .where(qRecentlyProduct.member.email.eq(getEmail))
                .orderBy(qRecentlyProduct.id.desc())
                .fetch();

        List<Bookmark> bookmark = queryFactory
                .selectFrom(qBookmark)
                .where(qBookmark.member.email.eq(getEmail))
                .fetch();

        RecentlyAndBookmarkSetDto recentlyAndBookmarkSetDto = new RecentlyAndBookmarkSetDto();


        List<RecentlySet> rs = recentlyAndBookmarkSetDto.getRecentlySets();
        for (int i = 0; i < Math.min(recentlyProducts.size(),5); i++) {
            if (recentlyProducts.get(i) != null) {
            RecentlySet recentlySet = new RecentlySet();
            recentlySet.setProductId(recentlyProducts.get(i).getProduct().getId());
            recentlySet.setProductName(recentlyProducts.get(i).getProduct().getProductName());
            recentlySet.setProductPrice(recentlyProducts.get(i).getProduct().getProductPrice());
            recentlySet.setProdcutImage(recentlyProducts.get(i).getProduct().getProductImage());
                rs.add(recentlySet);
            }else {
                break;
            }
        }


        List<BookmarkSet> bs = recentlyAndBookmarkSetDto.getBookmarkSets();
        for (Bookmark bookmark1 : bookmark) {
            BookmarkSet bookmarkSet = new BookmarkSet(bookmark1.getId(),
                    bookmark1.getMember().getId(),bookmark1.getProduct().getId(),bookmark1.getProduct().getProductImage(),
                    bookmark1.getProduct().getProductName(),bookmark1.getProduct().getProductPrice());
            bs.add(bookmarkSet);
        }


        return recentlyAndBookmarkSetDto;


    }



}
