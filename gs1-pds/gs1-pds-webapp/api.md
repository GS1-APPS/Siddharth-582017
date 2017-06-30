# GS1 PDS API

| URI               | HTTP Method   | Desription
--- | --- | ---
`/api/account/{gln}` | GET |
`/api/account/{gln}` | PUT | description
`/api/account/{gln}/apps` | GET | 
`/api/account/{gln}/subscription` | GET | 
`/api/account/{gln}/subscription/{appName}` | PUT |
`/api/account/{gln}/transactions` | GET |


| URI               | HTTP Method   | Desription
--- | --- | ---
`/api/product` | GET |
`/api/product/{gtin}` | GET |
`/api/product/{gtin}/validate` | POST |
`/api/products/bulkUpload` | POST |
`/api/product/{gtin}/create` | POST |
`/api/product/{gtin}/update` | POST |
`/api/product/{gtin}` | PUT |
`/api/product/{gtin}/info` | GET |
`/api/product/query` | GET |

| URI               | HTTP Method   | Desription
--- | --- | ---
`/api/search/productGpcTmList/{gpc}` | GET |
`/api/search/isoCountryList` | GET |
`/api/search/productBasedOnGpcAndTargetMarket/{gpc}` | GET |
`/api/search/productsForPagination/{gln}` | GET |
`/api/search/productById/{gtin}` | GET |


| URI               | HTTP Method   | Desription
--- | --- | ---
`/api/import` | GET |
`/api/import/{importId}` | GET |
`/api/import/{importId}` | DELETE |
`/api/import/{importId}/settings` | POST |
`/api/import/{importId}/confirm` | POST |
`/api/import/upload` | POST |


| URI               | HTTP Method   | Desription
--- | --- | ---
`/api/invoice` | GET |
`/api/invoice/{invoiceId}` | GET |
`/api/invoice/{invoiceId}/orders` | GET |
`/api/invoice/{invoiceId}/billinginfo` | PUT |
`/api/invoice/invoiceOrders` | POST |

| URI               | HTTP Method   | Desription
--- | --- | ---
`/api/payment` | GET |
`/api/payment/{paymentId}` | GET |
`/api/payment/{paymentId}/paid` | POST |
`/api/payment/payinvoices` | POST |

| URI               | HTTP Method   | Desription
--- | --- | ---
`/api/version` | GET |
`/api/time` | PUT |
`/api/order` | GET |
`/api/test/{testName}` | GET |
`/api/demo/deleteimports` | GET |
`/api/demo/deleteimports` | POST |
`/api/nonprod/product/{gtin}` | DELETE | 
`/api/registeredProductsCount/{gln}` | GET |
`/api/productList` | GET |
`/api/productListByDate` | GET |
